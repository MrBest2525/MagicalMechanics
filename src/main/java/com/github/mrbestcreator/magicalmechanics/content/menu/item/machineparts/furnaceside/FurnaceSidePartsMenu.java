package com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside;

import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SideInstance;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.side.FurnaceSideInstance;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import com.github.mrbestcreator.magicalmechanics.content.menu.util.PlayerInventoryUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FurnaceSidePartsMenu extends AbstractContainerMenu {
    
    public final MachineFrameBlockEntity blockEntity;
    public final Level level;
//    public final ContainerData data;
    
    public FurnaceSidePartsMenu(int containerId, Inventory playerInventory, IItemHandler dataInventory, ContainerData containerData, MachineFrameBlockEntity blockEntity) {
        super(ModMenus.FURNACE_SIDE_PARTS_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
//        this.data = containerData;
        this.addDataSlots(containerData);
        
        
        FurnaceSideInstance furnaceSide = new FurnaceSideInstance();
        if (blockEntity.sideInstance instanceof FurnaceSideInstance furnaceSideInstance) {
            furnaceSide = furnaceSideInstance;
        }
        
        // 1. プレイヤーインベントリ (Index 0 ~ 26) & ホットバー (Index 27 ~ 35)
        PlayerInventoryUtil.setPlayerInventorySlot(this, playerInventory);
        
        // 焼くもの投入
        this.addSlot(new InputSlot(dataInventory, 0, 0, 0, level));
        // 焼けたもの取り出し
        this.addSlot(new OutputSlot(playerInventory.player, dataInventory, 1, 0, 0, furnaceSide));
    }
    
    // --- サーバー側 ---
    public FurnaceSidePartsMenu(int containerId, Inventory playerInventory, MachineFrameBlockEntity blockEntity) {
        this(containerId, playerInventory, getInventory(blockEntity), blockEntity.data, blockEntity);
    }
    
    // --- クライアント側 ---
    public FurnaceSidePartsMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory, new ItemStackHandler(3), new SimpleContainerData(2), (MachineFrameBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        
        // 0番(マシン)からプレイヤーへ
        if (index >= 37) {
            if (!moveItemStackTo(sourceStack, 0, 36, true)) {
                return ItemStack.EMPTY;
            }
        }
        // プレイヤーから 36番(マシン)へ
        else {
            // レシピがあるかどうかチェックしてマシンへ送る試行
            SingleRecipeInput container = new SingleRecipeInput(slots.get(index).getItem());
            Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, container, level);
            if (recipe.isPresent()) {
                // マシンの 36番スロット (Input) へ試行
                if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                    // マシンがいっぱいなら、プレイヤーインベントリ内での移動に回す
                    if (!PlayerInventoryUtil.moveWithinPlayerInventory(this, sourceStack, index)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // レシピがないなら、プレイヤーインベントリ内（ホットバー ↔ メイン）で移動
                if (!PlayerInventoryUtil.moveWithinPlayerInventory(this, sourceStack, index)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        
        return copyStack;
    }
    
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, blockEntity.getBlockState().getBlock());
    }
    
    private static ItemStackHandler getInventory(MachineFrameBlockEntity be) {
        if (be.sideInstance instanceof FurnaceSideInstance furnaceside) {
            return furnaceside.inventory;
        }
        // nullを返すと呼び出し先でエラーになる可能性があるため、空のハンドラーを返す
        return new ItemStackHandler(2);
    }
    
    private static class InputSlot extends SlotItemHandler {
        private Level level;
        public InputSlot(IItemHandler itemHandler, int index, int x, int y, Level level) {
            super(itemHandler, index, x, y);
            this.level = level;
        }
        
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            SingleRecipeInput container = new SingleRecipeInput(stack);
            Optional<RecipeHolder<SmeltingRecipe>> recipe = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, container, level);
            return recipe.isPresent();
        }
    }
    
    private static class OutputSlot extends  SlotItemHandler {
        private final SideInstance side;
        
        public OutputSlot(Player player, IItemHandler itemHandler, int index, int x, int y, SideInstance side) {
            super(itemHandler, index, x, y);
            this.side = side; // FurnaceSideを渡す
        }
        
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false; // 出力スロットには何も置かせない
        }
        
        @Override
        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
            super.onTake(player, stack);
            // アイテムを取った時に経験値をドロップ
            if (side instanceof FurnaceSideInstance furnace) {
                float xp = furnace.getAndResetTotalXp();
                awardExperience(player, xp);
            }
        }
        
        private void awardExperience(Player player, float xp) {
            int amount = (int) xp;
            float fraction = xp - amount;
            if (fraction > 0 && Math.random() < fraction) {
                amount++;
            }
            if (amount > 0 && player.level() instanceof ServerLevel serverLevel) {
                ExperienceOrb.award(serverLevel, player.position(), amount);
            }
        }
    }
}
