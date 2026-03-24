package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SettingParts;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SettingPartsExecutionPhase;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SettingPartsInstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class SettingPartsManager {
    
    private final Map<UUID, ItemStack> items = new HashMap<>();
    // 1. 実体 (UUID -> Instance)
    private final Map<UUID, SettingPartsInstance> instances = new HashMap<>();
    
    // 2. ID索引 (ID -> UUIDs)
    private final Map<ResourceLocation, Set<UUID>> idIndex = new HashMap<>();
    
    // 3. 実行キャッシュ (ソート済み)
    private List<SettingPartsInstance> preTickCache = new ArrayList<>();
    private List<SettingPartsInstance> postTickCache = new ArrayList<>();
    
    /**
     * パーツを登録する唯一の入り口
     * ここを通ることで、インデックスとキャッシュが必ず同時に更新される
     */
    public UUID addPart(ItemStack itemStack) {
        UUID uuid = UUID.randomUUID();
        
        SettingPartsInstance instance = null;
        if (itemStack.getItem() instanceof SettingParts settingParts) {
            instance = settingParts.createInstance();
        }
        
        items.put(uuid, itemStack);
        
        instances.put(uuid, instance);
        
        // ID索引の更新
        idIndex.computeIfAbsent(instance != null ? instance.getInstanceId() : BuiltInRegistries.ITEM.getKey(itemStack.getItem()), k -> new HashSet<>()).add(uuid);
        
        // 実行順の再計算
        rebuildExecutionCache();
        
        return uuid;
    }
    
    public void removePart(UUID uuid) {
        // 1. アイテム実体とインスタンスを両方取り出す
        ItemStack stack = items.remove(uuid);
        SettingPartsInstance removedInstance = instances.remove(uuid);
        
        // 2. ID索引 (idIndex) の掃除
        // インスタンスがある場合とない場合（ただのアイテムの場合）で、キーが異なるので注意
        if (stack != null) {
            ResourceLocation id;
            if (removedInstance != null) {
                // 正規のパーツならインスタンスからIDを取得
                id = removedInstance.getInstanceId();
            } else {
                // ただのアイテムならレジストリからIDを取得 (addPart時のロジックと合わせる)
                id = BuiltInRegistries.ITEM.getKey(stack.getItem());
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
     * キャッシュを再構築する（パーツの抜き差し時に1回だけ呼ぶ）
     */
    private void rebuildExecutionCache() {
        // 1. ソート用の一時リストを作成
        List<SettingPartsInstance> allParts = new ArrayList<>();
        for (SettingPartsInstance inst : instances.values()) {
            if (inst != null) {
                allParts.add(inst);
            }
        }
        
        // 2. 「Group順 > SubPriority順」でソート
        // Phaseはリスト自体を分けるのでソート条件には入れない
        Comparator<SettingPartsInstance> sorter = Comparator
                .comparingInt((SettingPartsInstance p) -> p.getGroup().getOrder())
                .thenComparingInt(SettingPartsInstance::getSubPriority);
        
        allParts.sort(sorter);
        
        // 3. Phaseごとに振り分けてキャッシュを更新
        List<SettingPartsInstance> pre = new ArrayList<>();
        List<SettingPartsInstance> post = new ArrayList<>();
        
        for (SettingPartsInstance p : allParts) {
            if (p.getPhase() == SettingPartsExecutionPhase.PRE) pre.add(p);
            else post.add(p);
        }
        
        // 4. 不変リストとして保存（Tick中の安全のため）
        this.preTickCache = List.copyOf(pre);
        this.postTickCache = List.copyOf(post);
    }
    
    /**
     * Frame側のTickから呼ばれる取得メソッド
     */
    public List<SettingPartsInstance> getPreTickParts() {
        return preTickCache;
    }
    
    public List<SettingPartsInstance> getPostTickParts() {
        return postTickCache;
    }
    
    /**
     * キレイにする
     */
    public void clear() {
        items.clear();
        instances.clear();
        idIndex.clear();
        preTickCache.clear();
        postTickCache.clear();
    }
    
    // ==========================================
    // getter
    // ==========================================
    public ItemStack getItemStack(UUID uuid) {
        return items.get(uuid).copy();
    }
    
    public List<ItemStack> getItemStackList() {
        return (List<ItemStack>) items.values();
    }
    
    public List<SettingPartsInstance> getInstanceList() {
        return List.copyOf(instances.values());
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
            ItemStack stack = items.get(uuid);
            if (stack != null && !stack.isEmpty()) {
                compound.put("Stack", stack.save(provider));
            }
            
            // 3. Instanceの動的データの保存
            SettingPartsInstance instance = instances.get(uuid);
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
            ItemStack stack = ItemStack.parse(provider, compound.getCompound("Stack")).orElse(ItemStack.EMPTY);
            if (stack.isEmpty()) continue;
            
            // Instanceの生成と復元
            SettingPartsInstance instance = null;
            if (stack.getItem() instanceof SettingParts settingParts) {
                instance = settingParts.createInstance();
                if (compound.contains("InstanceData")) {
                    instance.load(compound.getCompound("InstanceData"), provider);
                }
            }
            
            // 内部データの再構築
            this.items.put(uuid, stack);
            this.instances.put(uuid, instance);
            
            // ID索引の再構築 (addPartのロジックと共通化)
            ResourceLocation id = (instance != null) ? instance.getInstanceId() : BuiltInRegistries.ITEM.getKey(stack.getItem());
            this.idIndex.computeIfAbsent(id, k -> new HashSet<>()).add(uuid);
        }
        
        // 最後に実行キャッシュを一度だけ再構築
        rebuildExecutionCache();
    }
}
