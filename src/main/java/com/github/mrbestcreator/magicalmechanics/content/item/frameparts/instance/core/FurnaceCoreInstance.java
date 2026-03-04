package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class FurnaceCoreInstance implements CoreInstance {
    
    private float biomeThermal;
    private boolean isBiomeThermal = false;
    private float thermal = 25;
    private final float minFireThermal = 500;
    private float fireThermal = minFireThermal;
    private float lastFireThermal = fireThermal;
    private int remainingBurnTime;
    private ItemStack burningItem = ItemStack.EMPTY;
    private ItemStack lastBurningItem = ItemStack.EMPTY;
    private boolean burning = false;
    private boolean isBurningRequired = true; //TODO テストで有効化 無効化忘れずに
    
    public ItemStackHandler inventory = new ItemStackHandler(1);
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag coreTag = new CompoundTag();
        coreTag.putFloat("thermal", thermal);
        coreTag.putBoolean("burning", burning);
        coreTag.putBoolean("isBurningRequired", isBurningRequired);
        if (!burningItem.isEmpty()) {
            coreTag.put("burningItem", burningItem.save(provider));
        }
        if (!lastBurningItem.isEmpty()) {
            coreTag.put("lastBurningItem", lastBurningItem.save(provider));
        }
        coreTag.put("inventory", inventory.serializeNBT(provider));
        coreTag.putFloat("fireThermal", fireThermal);
        coreTag.putFloat("lastFireThermal", lastFireThermal);
        coreTag.putInt("remainingBurnTime", remainingBurnTime);
        tag.put("FurnaceCore", coreTag);
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag coreTag = tag.getCompound("FurnaceCore");
        thermal = coreTag.getFloat("thermal");
        burning = coreTag.getBoolean("burning");
        isBurningRequired = coreTag.getBoolean("isBurningRequired");
        burningItem = ItemStack.parse(provider, coreTag.getCompound("burningItem")).orElse(ItemStack.EMPTY);
        lastBurningItem = ItemStack.parse(provider, coreTag.getCompound("lastBurningItem")).orElse(ItemStack.EMPTY);
        if (coreTag.contains("inventory", Tag.TAG_COMPOUND)) {
            inventory.deserializeNBT(provider, coreTag.getCompound("inventory"));
        }
        fireThermal = coreTag.getFloat("fireThermal");
        lastFireThermal = coreTag.getFloat("lastFireThermal");
        remainingBurnTime = coreTag.getInt("remainingBurnTime");
    }
    
    @Override
    public boolean tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        float lastThermal = thermal;
        // 初期化
        if (!isBiomeThermal) {
            biomeThermal = (level.getBiome(pos).value().getBaseTemperature() - 0.15f) * 30.77f;
            thermal = biomeThermal;
            isBiomeThermal = true;
        }
        
        // 燃焼が必要な時の処理
        if (isBurningRequired) {
            // 燃えていないかつ燃やす必要があるかつ燃えるアイテムが入ってる場合
            if (!burning && !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).getBurnTime(RecipeType.SMELTING) > 0) {
                burning = true;
                burningItem = inventory.extractItem(0, 1, false);
                remainingBurnTime = burningItem.getBurnTime(RecipeType.SMELTING);
                if (burningItem.getBurnTime(RecipeType.SMELTING) >= lastBurningItem.getBurnTime(RecipeType.SMELTING)) {
                    fireThermal = lastFireThermal;
                }
            }
        }
        
        // 燃焼処理
        if (burning) {
            fireThermal += (burningItem.getBurnTime(RecipeType.SMELTING) - (fireThermal - minFireThermal)) / 3 / 10;
            thermal += (float) ((fireThermal - thermal) * 0.1);
            
            remainingBurnTime --;
            if (remainingBurnTime <= 0) {
                burning = false;
                lastBurningItem = burningItem;
                burningItem = ItemStack.EMPTY;
                lastFireThermal = fireThermal;
                fireThermal = minFireThermal;
            }
        }
        
        // 自然冷却
        float diff = biomeThermal - thermal;
        // 20tick/s 前提で数十秒スケール
        float k = 0.005f;
        // 差が小さいほどさらに遅くする
        thermal += diff * k * Math.min(1.0f, Math.abs(diff) / 30.0f) * (float) (Math.min(Math.random() * 2, 1));
        
        return lastThermal != thermal;
    }
    
    @Override
    public AbstractContainerMenu getMenu(int id, Inventory inventory, FrameBlockEntity blockEntity) {
        return new FurnaceCorePartsMenu(id, inventory, blockEntity);
    }
    
    @Override
    public boolean supportsThermal() {
        return true;
    }
    
    @Override
    public float getThermal() {
        return thermal;
    }
    
    public boolean isBurning() {
        return burning;
    }
    
    public void setBurningRequired(boolean isBurningRequired) {
        this.isBurningRequired = isBurningRequired;
    }
    
    public boolean getIsBurningRequired() {
        return isBurningRequired;
    }
    
    public ItemStack getBurningItem() {
        return burningItem;
    }
}
