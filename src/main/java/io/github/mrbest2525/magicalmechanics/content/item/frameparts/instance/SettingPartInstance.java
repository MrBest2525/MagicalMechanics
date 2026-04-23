package io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance;

import io.github.mrbest2525.magicalmechanics.api.SourceType;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface SettingPartInstance {
    /**
     * InstanceのID
     */
    ResourceLocation getInstanceId();
    
    /**
     * 実行フェーズ (PRE / POST)
     */
    default SettingPartsExecutionPhase getPhase() {
        return SettingPartsExecutionPhase.PRE;
    }
    
    /**
     * 優先度グループ (EARLIEST / BEFORE / STANDARD / AFTER / LATEST)
     */
    default SettingPartsPriorityGroup getGroup() {
        return SettingPartsPriorityGroup.STANDARD;
    }
    
    /**
     * グループ内での詳細順序 (数値が小さいほど先かつ0が中心)
     */
    default int getSubPriority() {
        return 0;
    }
    
    
    void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    
    void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider);
    
    boolean tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MachineFrameBlockEntity blockEntity);
    
    void onAttached(@NotNull MachineFrameBlockEntity frame);
    
    void onDetached(@NotNull MachineFrameBlockEntity frame);
    
    default AbstractContainerMenu getMenu(int id, Inventory inventory, MachineFrameBlockEntity blockEntity) {
        return null;
    }
    
    /**
     * 座標送信の許可
     */
    default boolean canProvideSource(MachineFrameBlockEntity frame, SourceType type, Player player) {
        return false;
    }
    
    /**
     * 座標受付の許可
     */
    default boolean canAcceptTarget(MachineFrameBlockEntity frame, SourceType type, BlockPos pos, Player player) {
        return false;
    }
    
    /**
     * 座標を受け取った時の処理
     */
    default void onLinkEstablished(MachineFrameBlockEntity frame, SourceType type, BlockPos pos) {}
}
