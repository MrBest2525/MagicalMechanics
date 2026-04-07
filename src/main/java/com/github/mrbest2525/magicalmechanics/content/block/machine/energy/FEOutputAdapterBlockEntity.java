package com.github.mrbest2525.magicalmechanics.content.block.machine.energy;

import com.github.mrbest2525.magicalmechanics.api.SourceType;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableTarget;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import com.github.mrbest2525.magicalmechanics.util.math.MMLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class FEOutputAdapterBlockEntity extends BlockEntity implements IWirelessLinkableTarget, IEnergyStorage {
    
    private BlockPos linkedMachineFramePos;
    
    private final MMLong calcBuffer = new MMLong();
    private final MMLong requestBuffer = new MMLong();
    
    public FEOutputAdapterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FE_OUTPUT_ADAPTER.get(), pos, blockState);
    }
    
    // ==========================================
    // 💾 データの永続化 (NBT)
    // ==========================================
    
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (linkedMachineFramePos != null) {
            tag.put("LinkedPos", NbtUtils.writeBlockPos(linkedMachineFramePos));
        }
    }
    
    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("LinkedPos")) {
            this.linkedMachineFramePos = NbtUtils.readBlockPos(tag, "LinkedPos").orElse(null);
        }
    }
    
    // ==========================================
    // ⚙️ IEnergyStorage 実装
    // ==========================================
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        // 1. 基本バリデーション
        if (maxExtract <= 0 || linkedMachineFramePos == null || level == null) return 0;
        if (!level.isLoaded(linkedMachineFramePos)) return 0;
        
        BlockEntity blockEntity = level.getBlockEntity(linkedMachineFramePos);
        if (!(blockEntity instanceof MachineFrameBlockEntity machineFrame)) return 0;
        
        CoreInstance coreInstance = machineFrame.getCoreInstance();
        if (coreInstance == null || !coreInstance.supportsEnergy()) return 0;
        
        // 2. 取り出し要求量をバッファにセット
        requestBuffer.set(maxExtract);
        
        // 3. CoreInstance 側の consumeEnergy を利用
        // 内部で「現在の残量」と比較し、実際に取り出せる量を calcBuffer に書き込んでくれる
         return coreInstance.consumeEnergy(calcBuffer, requestBuffer, simulate).asInt();
    }
    
    @Override
    public int getEnergyStored() {
        if (linkedMachineFramePos == null || level == null || !level.isLoaded(linkedMachineFramePos)) return 0;
        BlockEntity be = level.getBlockEntity(linkedMachineFramePos);
        if (be instanceof MachineFrameBlockEntity mf && mf.getCoreInstance() != null) {
            return mf.getCoreInstance().getEnergy(requestBuffer).asInt();
        }
        return 0;
    }
    
    @Override
    public int getMaxEnergyStored() {
        if (linkedMachineFramePos == null || level == null || !level.isLoaded(linkedMachineFramePos)) return 0;
        BlockEntity be = level.getBlockEntity(linkedMachineFramePos);
        if (be instanceof MachineFrameBlockEntity mf && mf.getCoreInstance() != null) {
            return mf.getCoreInstance().getMaxEnergy(requestBuffer).asInt();
        }
        return 0;
    }
    
    @Override
    public boolean canExtract() {
        return true;
    }
    
    @Override
    public boolean canReceive() {
        return false;
    }
    
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // FE_INPUT_ADAPTER は自身の BlockEntityType
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK, // エネルギーの Capability を
                ModBlockEntities.FE_OUTPUT_ADAPTER.get(), // この BE 型に対して登録
                (be, side) -> be // この BE 自体が IEnergyStorage を実装しているので self を返す
        );
    }
    
    // ==========================================
    // ⚙️ IWirelessLinkableTarget 実装
    // ==========================================
    
    @Override
    public boolean canAcceptLinkTarget(SourceType type, BlockPos sourcePos, Player player) {
        return type == SourceType.MagicalFlux;
    }
    
    @Override
    public void setTargetLink(SourceType type, BlockPos pos) {
        this.linkedMachineFramePos = pos;
        this.setChanged();
    }
}
