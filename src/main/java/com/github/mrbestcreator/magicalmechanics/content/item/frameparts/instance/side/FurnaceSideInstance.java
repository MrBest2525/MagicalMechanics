package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.side;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.core.FurnaceCoreInstance;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside.FurnaceSidePartsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FurnaceSideInstance implements SideInstance {
    // TODO ロジックの作成
    
    // 1 = input
    // 2 = output
    public ItemStackHandler inventory = new ItemStackHandler(2);
    private ItemStack cookingItem = ItemStack.EMPTY;
    private ItemStack cookingResultItem = ItemStack.EMPTY;
    private int remainingCookingTime = 0;
    private int cookingProgres = 0;
    private int requiredTempMin = 0;
    private int requiredTempMax = 0;
    private float cookingTimeMultiplier = 0.85f;
    private float cookingItemXp = 0;
    private float totalCookingXp = 0;
    private State sideState = State.PAUSE;
    
    @Override
    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag sideTag = new CompoundTag();
        sideTag.put("inventory", inventory.serializeNBT(provider));
        if (!cookingItem.isEmpty()) {
            sideTag.put("cookingItem", cookingItem.save(provider));
        }
        if (!cookingResultItem.isEmpty()) {
            sideTag.put("cookingResultItem", cookingResultItem.save(provider));
        }
        sideTag.putInt("remainingCookingTime", remainingCookingTime);
        sideTag.putInt("cookingProgres", cookingProgres);
        sideTag.putInt("requiredTempMin", requiredTempMin);
        sideTag.putInt("requiredTempMax", requiredTempMax);
        sideTag.putFloat("totalCookingXp", totalCookingXp);
        sideTag.putFloat("cookingItemXp", cookingItemXp);
        
        tag.put("FurnaceSide", sideTag);
    }
    
    @Override
    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        CompoundTag sideTag = tag.getCompound("FurnaceSide");
        if (sideTag.contains("inventory", Tag.TAG_COMPOUND)) {
            inventory.deserializeNBT(provider, sideTag.getCompound("inventory"));
        }
        if (sideTag.contains("cookingItem", Tag.TAG_COMPOUND)) {
            cookingItem = ItemStack.parse(provider, sideTag.getCompound("cookingItem")).orElse(ItemStack.EMPTY);
        } else {
            cookingItem = ItemStack.EMPTY;
        }
        if (sideTag.contains("cookingResultItem", Tag.TAG_COMPOUND)) {
            cookingResultItem = ItemStack.parse(provider, sideTag.getCompound("cookingResultItem")).orElse(ItemStack.EMPTY);
        } else {
            cookingResultItem = ItemStack.EMPTY;
        }
        remainingCookingTime = sideTag.getInt("remainingCookingTime");
        cookingProgres= sideTag.getInt("cookingProgres");
        requiredTempMin = sideTag.getInt("requiredTempMin");
        requiredTempMax = sideTag.getInt("requiredTempMax");
        totalCookingXp = sideTag.getFloat("totalCookingXp");
        cookingItemXp = sideTag.getFloat("cookingItemXp");
    }
    
    @Override
    public boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity) {
        CoreInstance coreInstance = blockEntity.coreInstance;
        if (coreInstance == null) return false;
        if (cookingItem.isEmpty() && inventory.getStackInSlot(0).isEmpty()) return false;
        
        // 調理中ではなくInputにアイテムが入ってるときに調理を開始する
        SingleRecipeInput container = new SingleRecipeInput(inventory.getStackInSlot(0));
        Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, container, level);
        if (cookingItem.isEmpty() && !inventory.getStackInSlot(0).isEmpty() && recipe.isPresent()) {
            cookingItem = inventory.getStackInSlot(0);
            inventory.setStackInSlot(0, ItemStack.EMPTY);
            
            // 温度設定
            if (cookingItem.is(Tags.Items.FOODS)) {
                requiredTempMin = 500;
                requiredTempMax = 1200;
            } else if (cookingItem.is(Tags.Items.ORES)) {
                requiredTempMin = 1500;
                requiredTempMax = 10000;
            } else {
                requiredTempMin = 800;
                requiredTempMax = 1750;
            }
            
            recipe.ifPresent(holder -> {
                int baseTime = holder.value().getCookingTime();
                int count = cookingItem.getCount();
                
                // 焼くのにかかる時間設定
                if (count <= 1) {
                    remainingCookingTime = baseTime;
                } else {
                    // Math.pow を使用して加速カーブを計算
                    remainingCookingTime = (int) (baseTime * Math.pow(count, cookingTimeMultiplier));
                }
                
                // 調理後ドロップする経験値のキャッシュ
                cookingItemXp = holder.value().getExperience() * count;
                
                // 調理後のアイテムのキャッシュ
                cookingResultItem = holder.value().getResultItem(level.registryAccess());
                cookingResultItem.setCount(count);
                
                // 焼き時間の進行
                cookingProgres = 0;
            });
            
        }
        
        if (coreInstance.getThermal() >= requiredTempMin && coreInstance.getThermal() <= requiredTempMax) {
            // 十分な温度がありまだ調理が終わっていないなら調理プロセスを進める
            if (remainingCookingTime > cookingProgres) {
                cookingProgres++;
            }
            
        } else if (coreInstance.getThermal() > requiredTempMax && !(cookingProgres >= remainingCookingTime)) {
            // 温度が高過ぎたら処理を行わない
            sideState = State.HIGH_TEMPERATURE;
            return true;
            
        } else if (coreInstance.getThermal() < requiredTempMin && !(cookingProgres >= remainingCookingTime)) {
            // 温度が低い場合 & 調理が完了していない場合
            sideState = State.LOW_TEMPERATURE;
            // FurnaceCoreなら着火処理を行う
            if (coreInstance instanceof FurnaceCoreInstance furnaceCoreInstance) {
                if (!furnaceCoreInstance.isBurning()) {
                    furnaceCoreInstance.setBurningRequired(true);
                }
            }
            return true;
        }
        
        // remainingCookingTime <= cookingProgresなら調理を終わりにし取得経験値量を増やす
        if (remainingCookingTime <= cookingProgres) {
            if (!cookingResultItem.isEmpty() && inventory.getStackInSlot(1).isEmpty()) {
                inventory.setStackInSlot(1, cookingResultItem);
                totalCookingXp += cookingItemXp;
                
                // 初期化処理
                cookingResultItem = ItemStack.EMPTY;
                cookingItem = ItemStack.EMPTY;
                cookingProgres = 0;
                cookingItemXp = 0;
                
            }
        }
        
        return true;
    }
    
    @Override
    public void onAttached(@NotNull MachineFrameBlockEntity frame) {
    
    }
    
    @Override
    public void onDetached(@NotNull MachineFrameBlockEntity frame) {
        Level level = frame.getLevel();
        BlockPos pos = frame.getBlockPos();
        if (level != null && !level.isClientSide) {
            // 保持していたアイテムをドロップ
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cookingItem);
        }
    }
    
    @Override
    public AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {
        return new FurnaceSidePartsMenu(id, inventory, blockEntity);
    }
    
    public float getAndResetTotalXp() {
        float xp = this.totalCookingXp;
        this.totalCookingXp = 0; // 引き出したのでリセット
        return xp;
    }
    
    public ItemStack getCookingItem() {
        return cookingItem;
    }
    
    private enum State{
        HIGH_TEMPERATURE,
        LOW_TEMPERATURE,
        REFINING,
        PAUSE
    }
}
