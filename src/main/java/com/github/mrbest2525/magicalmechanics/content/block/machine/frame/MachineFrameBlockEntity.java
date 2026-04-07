package com.github.mrbest2525.magicalmechanics.content.block.machine.frame;

import com.github.mrbest2525.magicalmechanics.api.SourceType;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableSource;
import com.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableTarget;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.FrameSide;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.IWirelessMFProvider;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchInteractable;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import com.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsMenu;
import com.github.mrbest2525.magicalmechanics.content.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MachineFrameBlockEntity extends BlockEntity implements WrenchInteractable, IWirelessLinkableSource, IWirelessLinkableTarget {
    // TODO Save&LoadでSettingPartのデータが消える(changeDataの呼び忘れかも)
    public final SettingPartsManager settingParts = new SettingPartsManager(this);
    
    private final Map<MachineFrameSlot, ItemStack> parts = new EnumMap<>(MachineFrameSlot.class);
    public CoreInstance coreInstance;
    public SideInstance sideInstance;
    
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> {
                    if (MachineFrameBlockEntity.this.coreInstance instanceof FurnaceCoreInstance furnaceCoreInstance) {
                        yield furnaceCoreInstance.isBurning() ? 1 : 0;
                    }
                    yield 0;
                }
                case 1 -> Float.floatToIntBits(MachineFrameBlockEntity.this.coreInstance.getThermal());
                default -> 0;
            };
        }
        
        @Override
        public void set(int i, int i1) {
        
        }
        
        @Override
        public int getCount() {
            return 2;
        }
    };
    
    public MachineFrameBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_FRAME.get(), pos, blockState);
        for (MachineFrameSlot slot : MachineFrameSlot.values()) {
            parts.put(slot, ItemStack.EMPTY);
        }
    }
    
    public void setPart(MachineFrameSlot slot, ItemStack item) {
        parts.put(slot, item);
        setChangeData();
    }
    
    public ItemStack getPart(MachineFrameSlot slot) {
        return parts.get(slot);
    }
    
    public List<ItemStack> getParts() {
        return List.copyOf(parts.values());
    }
    
    public CoreInstance getCoreInstance() {
        return coreInstance;
    }
    
    public SideInstance getSideInstance() {
        return sideInstance;
    }
    
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // TODO PartsのTagをPartsTags.(Core | Side)に保存するように(それぞれのタグを渡すように)した方がいいかも？
        CompoundTag partsData = new CompoundTag();
        
        partsData.put("SettingPartsData", settingParts.save(provider));
        
        // CoreのSave
        if (coreInstance != null) {
            CompoundTag coreTag = new CompoundTag();
            coreInstance.save(coreTag, provider);
            partsData.put("CoreData", coreTag);
        }
        // SideのSave
        if (sideInstance != null) {
            CompoundTag sideTag = new CompoundTag();
            sideInstance.save(sideTag, provider);
            partsData.put("SideData", sideTag);
        }
        
        tag.put("PartsData", partsData);
        
        CompoundTag partsTag = new CompoundTag();
        for (Map.Entry<MachineFrameSlot, ItemStack> entry : parts.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                partsTag.put(entry.getKey().name(), entry.getValue().save(provider));
            }
        }
        tag.put("Parts", partsTag);
        
    }
    
    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        
        parts.clear();
        CompoundTag partsTag = tag.getCompound("Parts");
        for (MachineFrameSlot slot : MachineFrameSlot.values()) {
            if (partsTag.contains(slot.name())) {
                parts.put(slot, ItemStack.parse(provider, partsTag.getCompound(slot.name())).orElse(ItemStack.EMPTY));
            } else {
                parts.put(slot, ItemStack.EMPTY);
            }
        }
        
        CompoundTag partsData = tag.getCompound("PartsData");
        
        if (partsData.contains("SettingPartsData", Tag.TAG_LIST)) {
            this.settingParts.load(partsData.getList("SettingPartsData", Tag.TAG_COMPOUND), provider);
        }
        
        // CoreのLoad
        if (parts.get(MachineFrameSlot.CORE).getItem() instanceof FrameCore frameCore) {
            coreInstance = frameCore.createInstance(this);
            CompoundTag coreTag = partsData.getCompound("CoreData");
            coreInstance.load(coreTag, provider);
        }
        // SideのLoad
        if (parts.get(MachineFrameSlot.SIDE).getItem() instanceof FrameSide frameSide) {
            sideInstance = frameSide.createInstance(this);
            CompoundTag sideTag = partsData.getCompound("SideData");
            sideInstance.load(sideTag, provider);
        }
    }
    
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level == null) return;
        
        boolean isChangeData = false;
        
        if (level.isClientSide()) {
        
        } else {
            
            settingParts.getPreTickParts().forEach(settingPartsInstance -> settingPartsInstance.tick(level, pos, state, this));
            
            if (coreInstance != null) {
                if (coreInstance.tick(level, pos, state, this)) {
                    isChangeData = true;
                }
            }
            if (sideInstance != null) {
                if (sideInstance.tick(level, pos, state, this)) {
                    isChangeData = true;
                }
            }
            
            settingParts.getPostTickParts().forEach(settingPartsInstance -> settingPartsInstance.tick(level, pos, state, this));
        }
        
        settingParts.tick(level, pos, state);
        
        if (isChangeData) {
            setChangeData();
        }
    }
    
    public boolean tryInsert(MachineFrameSlot slot, ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        ItemStack current = parts.get(slot);
        if (!current.isEmpty()) return false;
        
        switch (slot) {
            case SIDE:
                if (!(stack.getItem() instanceof FrameSide)) return false;
                break;
            case CORE:
                if (!(stack.getItem() instanceof FrameCore)) return false;
                break;
        }
        
        ItemStack inserted = stack.copy();
        inserted.setCount(1);
        parts.put(slot, inserted);
        
        stack.shrink(1);
        setChangeData();
        
        switch (slot) {
            case CORE:
                if (parts.get(slot).getItem() instanceof FrameCore frameCore) {
                    coreInstance = frameCore.createInstance(this);
                    coreInstance.onAttached(this);
                }
                break;
            case SIDE:
                if (parts.get(slot).getItem() instanceof FrameSide frameSide) {
                    sideInstance = frameSide.createInstance(this);
                    sideInstance.onAttached(this);
                }
                break;
        }
        
        return true;
    }
    
    public ItemStack tryExtract(MachineFrameSlot slot) {
        ItemStack current = parts.get(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;
        
        parts.put(slot, ItemStack.EMPTY);
        setChangeData();
        
        switch (slot) {
            case CORE:
                coreInstance.onDetached(this);
                coreInstance = null;
                break;
            case SIDE:
                sideInstance.onDetached(this);
                sideInstance = null;
                break;
        }
        
        return current.copy();
    }
    
    @Override
    public InteractionResult onWrenchUse(UseOnContext context) {
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;
        
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.CONSUME;
        if (player.getOffhandItem().isEmpty()) {
            player.getCooldowns().addCooldown(context.getItemInHand().getItem(), 5);
            return InteractionResult.CONSUME;
        }
        
        ItemStack offhandItem = player.getOffhandItem();
        ItemStack itemStack = context.getItemInHand();
        
        boolean inserted;
        if (!offhandItem.is(ModTags.Items.WRENCH_ITEMS)) {
            
            inserted = switch (WrenchItem.getMode(itemStack)) {
                case SIDE -> tryInsert(MachineFrameSlot.SIDE, offhandItem);
                case CORE -> tryInsert(MachineFrameSlot.CORE, offhandItem);
            };
        } else {
            ItemStack out = switch (WrenchItem.getMode(itemStack)) {
                case SIDE -> tryExtract(MachineFrameSlot.SIDE);
                case CORE -> tryExtract(MachineFrameSlot.CORE);
            };
            if (!out.isEmpty()) {
                if (!player.getInventory().add(out)) {
                    player.drop(out, false);
                }
                inserted = true;
            } else {
                inserted = false;
            }
        }
        
        if (!inserted) player.getCooldowns().addCooldown(context.getItemInHand().getItem(), 5);
        
        return inserted
                ? InteractionResult.CONSUME
                : InteractionResult.PASS;
    }
    
    public void onRemove() {
        settingParts.getInstanceList().forEach(instance -> instance.onDetached(this));
        
        if (coreInstance != null) {
            coreInstance.onDetached(this);
            coreInstance = null;
        }
        if (sideInstance != null) {
            sideInstance.onDetached(this);
            sideInstance = null;
        }
    }
    
    public void clearContent() {
        settingParts.clear();
        parts.clear();
    }
    
    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries); // 全データをタグに書き込む
        return tag;
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        // サーバーからクライアントへ送るパケットを生成
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public void onDataPacket(@NotNull Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
        // サーバーからパケットが届いた時にNBTを読み込む
        CompoundTag tag = pkt.getTag();
        this.loadAdditional(tag, lookupProvider);
    }
    
    // 念のためこちらも（チャンク読み込み時などの同期）
    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        this.loadAdditional(tag, lookupProvider);
    }
    
    private void setChangeData() {
        // TODO sendBlockUpdated()の呼び出し条件の厳格化(クライアントに伝えなければならないものだけに制限)
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
        setChanged();
    }
    
    public AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {
        return new FrameBlockSettingPartsMenu(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), id, inventory, blockEntity);
    }
    
    // ========================================
    // SettingParts
    // ========================================
    public SettingPartsManager getSettingPartsManager() {
        return this.settingParts;
    }
    
    
    // ========================================
    // Linker
    // ========================================
    @Override
    public boolean canAcceptLinkSource(SourceType type, Player player) {
        return type == SourceType.MagicalFlux &&
                sideInstance instanceof IWirelessMFProvider provider;
    }
    
    @Override
    public BlockPos getSourcePos(SourceType type) {
        return this.worldPosition;
    }
    
    @Override
    public boolean canAcceptLinkTarget(SourceType type, BlockPos sourcePos, Player player) {
        return type == SourceType.MagicalFlux && settingParts.getInstanceList().stream()
                .anyMatch(instance -> instance.canAcceptTarget(this, type, sourcePos, player));
    }
    
    @Override
    public void setTargetLink(SourceType type, BlockPos pos) {
        settingParts.getInstanceList().forEach(instance -> instance.onLinkEstablished(this, type, pos));
        setChangeData();
    }
}
