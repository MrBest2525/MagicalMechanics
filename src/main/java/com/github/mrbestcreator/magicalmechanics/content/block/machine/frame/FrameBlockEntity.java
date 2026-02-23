package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.FrameCore;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.FrameParts;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.PartsInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchInteractable;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbestcreator.magicalmechanics.content.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class FrameBlockEntity extends BlockEntity implements WrenchInteractable {
    
    private final Map<FrameSlot, ItemStack> parts = new EnumMap<>(FrameSlot.class);
    private CoreInstance coreInstance;
    private PartsInstance partsInstance;
    
    public FrameBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MACHINE_FRAME.get(), pos, blockState);
        for (FrameSlot slot : FrameSlot.values()) {
            parts.put(slot, ItemStack.EMPTY);
        }
    }
    
    public void setPart(FrameSlot slot, ItemStack item) {
        parts.put(slot, item);
        setChangeData();
    }
    
    public ItemStack getPart(FrameSlot slot) {
        return parts.get(slot);
    }
    
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        
        CompoundTag partsTag = new CompoundTag();
        for (Map.Entry<FrameSlot, ItemStack> entry : parts.entrySet()) {
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
        for (FrameSlot slot : FrameSlot.values()) {
            if (partsTag.contains(slot.name())) {
                parts.put(slot, ItemStack.parse(provider, partsTag.getCompound(slot.name())).orElse(ItemStack.EMPTY));
            } else {
                parts.put(slot, ItemStack.EMPTY);
            }
        }
        if (parts.get(FrameSlot.CORE).getItem() instanceof FrameCore frameCore) {
            coreInstance = frameCore.createInstance();
        }
        if (parts.get(FrameSlot.SIDE).getItem() instanceof FrameParts frameParts) {
            partsInstance = frameParts.createInstance();
        }
    }
    
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level == null) return;
        
        if (level.isClientSide()) {
        
        } else {
            if (coreInstance != null) {
                coreInstance.tick(level, pos, state, this);
                coreInstance.getThermal();
            }
            if (partsInstance != null) {
                partsInstance.tick(level, pos, state, this);
            }
        }
    }
    
    public boolean tryInsert(FrameSlot slot, ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        ItemStack current = parts.get(slot);
        if (!current.isEmpty()) return false;
        
        switch (slot) {
            case SIDE:
                if (!stack.is(ModTags.Items.FRAME_SIDE_PARTS)) return false;
                break;
            case CORE:
                if (!stack.is(ModTags.Items.FRAME_CORE_PARTS)) return false;
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
                    coreInstance = frameCore.createInstance();
                }
                break;
            case SIDE:
                if (parts.get(slot).getItem() instanceof FrameParts frameParts) {
                    partsInstance = frameParts.createInstance();
                }
                break;
        }
        
        return true;
    }
    
    public ItemStack tryExtract(FrameSlot slot) {
        ItemStack current = parts.get(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;
        
        parts.put(slot, ItemStack.EMPTY);
        setChangeData();
        
        switch (slot) {
            case CORE:
                coreInstance = null;
                break;
            case SIDE:
                partsInstance = null;
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
                case SIDE -> tryInsert(FrameSlot.SIDE, offhandItem);
                case CORE -> tryInsert(FrameSlot.CORE, offhandItem);
            };
        } else {
            ItemStack out = switch (WrenchItem.getMode(itemStack)) {
                case SIDE -> tryExtract(FrameSlot.SIDE);
                case CORE -> tryExtract(FrameSlot.CORE);
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
    
    private void setChangeData() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
