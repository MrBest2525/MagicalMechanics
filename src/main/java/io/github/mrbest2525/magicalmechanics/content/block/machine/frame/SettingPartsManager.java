package io.github.mrbest2525.magicalmechanics.content.block.machine.frame;

import com.mojang.logging.LogUtils;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPart;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPartInstance;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SettingPartsExecutionPhase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.*;

public class SettingPartsManager {
    
    private final Logger LOGGER = LogUtils.getLogger();
    
    private final MachineFrameBlockEntity blockEntity;
    
    private final Map<UUID, PartItemStack> items = new HashMap<>();
    // 1. 実体 (UUID -> Instance)
    private final Map<UUID, SettingPartInstance> instances = new HashMap<>();
    
    // 2. ID索引 (ID -> UUIDs)
    private final Map<ResourceLocation, Set<UUID>> idIndex = new HashMap<>();
    
    // 3. 実行キャッシュ (ソート済み)
    private List<SettingPartInstance> preTickCache = new ArrayList<>();
    private List<SettingPartInstance> postTickCache = new ArrayList<>();
    
    public SettingPartsManager(MachineFrameBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }
    
    /**
     * データ保持用class
     */
    public static class PartItemStack {
        private final ItemStack itemStack;
        private int count;
        
        public int deathTimer = 0;
        
        public static final PartItemStack EMPTY = new PartItemStack(ItemStack.EMPTY, 0);
        
        public PartItemStack(ItemStack itemStack) {
            this.count = itemStack.getCount();
            this.itemStack = itemStack.copy();
            this.itemStack.setCount(1);
            itemStack.setCount(0);
        }
        
        public boolean tryAddItemStack(ItemStack itemStack) {
            // アイテムとコンポーネント（NBT等）が一致するかチェック
            if (ItemStack.isSameItemSameComponents(this.itemStack, itemStack)) {
                count += itemStack.getCount();
                itemStack.setCount(0);
                return true;
            }
            return false;
        }
        
        /**
         * NBTへの書き込み
         */
        public CompoundTag save(HolderLookup.Provider provider) {
            CompoundTag nbt = new CompoundTag();
            
            // 1. ItemStackの「型紙」を保存 (count 1の状態)
            nbt.put("Template", this.itemStack.save(provider));
            
            // 2. 真の個数を int として保存 (21億までOK)
            nbt.putInt("ActualCount", this.count);
            
            nbt.putInt("DeathTimer", this.deathTimer);
            
            return nbt;
        }
        
        /**
         * NBTからの復元用スタティックメソッド
         */
        public static PartItemStack load(CompoundTag nbt, HolderLookup.Provider provider) {
            ItemStack stack = ItemStack.parse(provider, nbt.getCompound("Template")).orElse(ItemStack.EMPTY);
            if (stack.isEmpty()) return null;
            
            int actualCount = nbt.getInt("ActualCount");
            
            int deathTimer = nbt.getInt("DeathTimer");
            
            // 復元用コンストラクタなどを通して生成
            return new PartItemStack(stack, actualCount, deathTimer);
        }
        
        // 内部的な復元用コンストラクタ
        private PartItemStack(ItemStack stack, int count) {
            this.itemStack = stack.copyWithCount(1);
            this.count = count;
        }
        
        private PartItemStack(ItemStack stack, int count, int deathTimer) {
            this.itemStack = stack.copyWithCount(1);
            this.count = count;
            this.deathTimer = deathTimer;
        }
        
        public boolean isEmpty() {
            return count < 1;
        }
        
        public PartItemStack copy() {
            return new PartItemStack(itemStack, count);
        }
        
        public ItemStack getItemStack() {
            return itemStack.copy();
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
        
        /**
         * ネットワーク転送用のシリアライザ
         */
        public static final StreamCodec<RegistryFriendlyByteBuf, PartItemStack> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, p -> p.itemStack,       // ItemStack部分の読み書き
                ByteBufCodecs.VAR_INT, p -> p.count,            // int(個数)部分の読み書き
                PartItemStack::new                              // 読み取ったデータで新しいインスタンスを作る
        );
    }
    
    /**
     * パーツを登録する唯一の入り口
     * ここを通ることで、インデックスとキャッシュが必ず同時に更新される
     */
    public UUID addPart(ItemStack itemStack) {
        // TODO 同じItemを2st以上入れると中身が確認できなくなるバグ発生(後壊すとクラッシュ)保存をclass作成して保持するItemStackと個数のintに分けるべき
        // 1. 同一アイテム（NBT含む）の検索と合流
        for (Map.Entry<UUID, PartItemStack> entry : items.entrySet()) {
            PartItemStack existing = entry.getValue();
            
            // アイテムとコンポーネント（NBT等）が一致するかチェック
            if (existing.tryAddItemStack(itemStack)) {
                rebuildExecutionCache();
                return entry.getKey();
            }
        }
        
        UUID uuid = UUID.randomUUID();
        
        SettingPartInstance instance = null;
        if (itemStack.getItem() instanceof SettingPart settingPart) {
            instance = settingPart.createInstance();
        }
        
        PartItemStack saveStack = new PartItemStack(itemStack);
        
        items.put(uuid, saveStack);
        
        instances.put(uuid, instance);
        
        // ID索引の更新
        idIndex.computeIfAbsent(instance != null ? instance.getInstanceId() : BuiltInRegistries.ITEM.getKey(saveStack.getItemStack().getItem()), k -> new HashSet<>()).add(uuid);
        
        // 実行順の再計算
        rebuildExecutionCache();
        
        return uuid;
    }
    
    public void removePart(UUID uuid) {
        // 1. アイテム実体とインスタンスを両方取り出す
        PartItemStack stack = items.remove(uuid);
        SettingPartInstance removedInstance = instances.remove(uuid);
        
        // 2. ID索引 (idIndex) の掃除
        // インスタンスがある場合とない場合（ただのアイテムの場合）で、キーが異なるので注意
        if (stack != null) {
            ResourceLocation id;
            if (removedInstance != null) {
                // 正規のパーツならインスタンスからIDを取得
                id = removedInstance.getInstanceId();
            } else {
                // ただのアイテムならレジストリからIDを取得 (addPart時のロジックと合わせる)
                id = BuiltInRegistries.ITEM.getKey(stack.getItemStack().getItem());
            }
            
            // 索引からこのUUIDを削除
            Set<UUID> uuids = idIndex.get(id);
            if (uuids != null) {
                uuids.remove(uuid);
                // Setが空になったらメモリ節約のためにMapからキーごと消す
                if (uuids.isEmpty()) {
                    idIndex.remove(id);
                }
            }
        }
        
        // 3. 実行キャッシュを更新
        // これを呼ばないと、取り外したはずのパーツが次のTickでも動いてしまう
        rebuildExecutionCache();
    }
    
    /**
     * 指定した個数を上限としてパーツを引き出す
     * @param uuid 取り出すパーツの識別子
     * @param limit 取り出す最大個数 (例: 64)
     * @return 引き出されたアイテムスタック (存在しない場合は EMPTY)
     */
    public ItemStack extractLimited(UUID uuid, int limit) {
        // 1. 指定されたUUIDのデータが存在するか確認
        if (!this.items.containsKey(uuid)) {
            return ItemStack.EMPTY;
        }
        
        PartItemStack partData = this.items.get(uuid);
        int currentCount = partData.getCount();
        
        // 2. 取り出す個数を決定 (在庫と上限の小さい方)
        int toExtract = Math.min(currentCount, limit);
        
        // 3. 返却用のスタックを作成 (元のアイテムのコピーに個数を設定)
        ItemStack resultStack = partData.getItemStack().copyWithCount(toExtract);
        
        // 4. 残り個数を計算
        int remaining = currentCount - toExtract;
        
        if (remaining > 0) {
            // 残りがある場合は更新
            this.items.get(uuid).setCount(remaining);
        }
        
        // キャッシュの更新
        rebuildExecutionCache();
        
        return resultStack;
    }
    
    public int extractPartCount(UUID uuid, int amount) {
        PartItemStack part = items.get(uuid);
        if (part == null) return 0;
        
        int current = part.getCount();
        int toExtract = Math.min(current, amount); // 在庫以上は取れない
        
        part.setCount(current - toExtract); // 在庫を減らす
        
        // キャッシュの更新
        rebuildExecutionCache();
        
        return toExtract; // 「実際に引き抜けた個数」を報告する
    }
    
    
    /**
     * キャッシュを再構築する（パーツの抜き差し時に1回だけ呼ぶ）
     */
    private void rebuildExecutionCache() {
        // 1. ソート用の一時リストを作成
        List<SettingPartInstance> allParts = new ArrayList<>();
        instances.forEach((uuid, settingPartsInstance) -> {
            PartItemStack stack = items.get(uuid);
            if (settingPartsInstance != null && stack != null && stack.getCount() > 0) {
                allParts.add(settingPartsInstance);
            }
        });
        
        // 2. 「Group順 > SubPriority順」でソート
        // Phaseはリスト自体を分けるのでソート条件には入れない
        Comparator<SettingPartInstance> sorter = Comparator
                .comparingInt((SettingPartInstance p) -> p.getGroup().getOrder())
                .thenComparingInt(SettingPartInstance::getSubPriority);
        
        allParts.sort(sorter);
        
        // 3. Phaseごとに振り分けてキャッシュを更新
        List<SettingPartInstance> pre = new ArrayList<>();
        List<SettingPartInstance> post = new ArrayList<>();
        
        for (SettingPartInstance p : allParts) {
            if (p.getPhase() == SettingPartsExecutionPhase.PRE) pre.add(p);
            else post.add(p);
        }
        
        // 4. 不変リストとして保存（Tick中の安全のため）
        this.preTickCache = List.copyOf(pre);
        this.postTickCache = List.copyOf(post);
        
        blockEntity.setChanged();
    }
    
    /**
     * Frame側のTickから呼ばれる取得メソッド
     */
    public List<SettingPartInstance> getPreTickParts() {
        return preTickCache;
    }
    
    public List<SettingPartInstance> getPostTickParts() {
        return postTickCache;
    }
    
    /**
     * キレイにする
     */
    public void clear() {
        items.clear();
        instances.clear();
        idIndex.clear();
        preTickCache = List.of();
        postTickCache = List.of();
        
        blockEntity.setChanged();
    }
    
    // ==========================================
    // getter
    // ==========================================
    public PartItemStack getPartItemStack(UUID uuid) {
        PartItemStack part = items.get(uuid);
        return (part != null) ? part.copy() : PartItemStack.EMPTY.copy();
    }
    
    public int getItemStackCount(UUID uuid) {
        PartItemStack part = items.get(uuid);
        return (part != null) ? part.getCount() : 0;
    }
    
    public List<PartItemStack> getItemStackList() {
        return new ArrayList<>(items.values());
    }
    
    public Map<UUID, PartItemStack> getUUIDtoPartsItemStackMap() {
        return Map.copyOf(items);
    }
    
    public List<SettingPartInstance> getInstanceList() {
        return instances.values().stream()
                .filter(Objects::nonNull)
                .toList();
    }
    
    // ==========================================
    // 💾 保存 (NBT Serialization)
    // ==========================================
    public ListTag save(HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        
        for (UUID uuid : items.keySet()) {
            CompoundTag compound = new CompoundTag();
            
            // 1. UUIDの保存
            compound.putUUID("UUID", uuid);
            
            // 2. ItemStackの保存
            PartItemStack stack = items.get(uuid);
            if (stack != null) {
                compound.put("Stack", stack.save(provider));
            }
            
            // 3. Instanceの動的データの保存
            SettingPartInstance instance = instances.get(uuid);
            if (instance != null) {
                CompoundTag data = new CompoundTag();
                instance.save(data, provider);
                compound.put("InstanceData", data);
            }
            
            list.add(compound);
        }
        return list;
    }
    
    // ==========================================
    // 📥 復元 (NBT Deserialization)
    // ==========================================
    public void load(ListTag list, HolderLookup.Provider provider) {
        // 現在の状態をクリア
        this.items.clear();
        this.instances.clear();
        this.idIndex.clear();
        
        for (int i = 0; i < list.size(); i++) {
            CompoundTag compound = list.getCompound(i);
            UUID uuid = compound.getUUID("UUID");
            
            // ItemStackの復元
            PartItemStack stack = PartItemStack.load(compound.getCompound("Stack"), provider);
            if (stack == null || stack.isEmpty()) continue;
            
            // Instanceの生成と復元
            SettingPartInstance instance = null;
            if (stack.getItemStack().getItem() instanceof SettingPart settingPart) {
                instance = settingPart.createInstance();
                if (compound.contains("InstanceData")) {
                    instance.load(compound.getCompound("InstanceData"), provider);
                }
            }
            
            // 内部データの再構築
            this.items.put(uuid, stack);
            this.instances.put(uuid, instance);
            
            // ID索引の再構築 (addPartのロジックと共通化)
            ResourceLocation id = (instance != null) ? instance.getInstanceId() : BuiltInRegistries.ITEM.getKey(stack.getItemStack().getItem());
            this.idIndex.computeIfAbsent(id, k -> new HashSet<>()).add(uuid);
        }
        
        // 最後に実行キャッシュを一度だけ再構築
        rebuildExecutionCache();
    }
    
    // ==========================================
    // Tick
    // ==========================================
    public void tick(Level level, BlockPos pos, BlockState state) {
        boolean changed = false;
        
        // iteratorを使えば削除と同時に他のMapも掃除できる
        Iterator<Map.Entry<UUID, PartItemStack>> it = items.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, PartItemStack> entry = it.next();
            UUID uuid = entry.getKey();
            PartItemStack part = entry.getValue();
            
            if (part.getCount() <= 0) {
                part.deathTimer++;
                if (part.deathTimer >= 6000) { // 5分経過
                    it.remove(); // itemsから削除
                    this.instances.remove(uuid); // 対応する動作インスタンスも削除
                    
                    // idIndexからも消す（removePartのロジックと同様の処理が必要）
                    removePart(uuid);
                    
                    changed = true;
                }
            } else {
                part.deathTimer = 0;
            }
        }
        
        if (changed) {
            rebuildExecutionCache();
        }
        
        blockEntity.setChanged();
    }
    
    
    /**
     * サーバーから送られてきたパーツリストで、クライアント側のデータを同期する
     */
    public void setAllPartsFromServer(Map<UUID, PartItemStack> newParts) {
        this.items.clear();
        this.instances.clear();
        this.idIndex.clear();
        
        this.items.putAll(newParts);
        
        for (Map.Entry<UUID, PartItemStack> entry : items.entrySet()) {
            UUID uuid = entry.getKey();
            ItemStack stack = entry.getValue().getItemStack();
            
            if (stack.getItem() instanceof SettingPart settingPart) {
                SettingPartInstance instance = settingPart.createInstance();
                
                // ⭐ ここで null チェックを入れる
                if (instance != null) {
                    this.instances.put(uuid, instance);
                    ResourceLocation id = instance.getInstanceId();
                    this.idIndex.computeIfAbsent(id, k -> new java.util.HashSet<>()).add(uuid);
                } else {
                    // デバッグ用にログを出しておくと原因究明が捗ります
                    LOGGER.debug("Failed to create instance for part: " + stack.getItem());
                }
            }
        }
        rebuildExecutionCache();
    }
}
