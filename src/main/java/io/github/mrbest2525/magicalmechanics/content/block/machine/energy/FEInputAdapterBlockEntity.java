package io.github.mrbest2525.magicalmechanics.content.block.machine.energy;

import io.github.mrbest2525.magicalmechanics.api.ISourceTypeProvider;
import io.github.mrbest2525.magicalmechanics.api.SourceType;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.block.mf.wireless.IWirelessLinkableTarget;
import io.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.CoreInstance;
import io.github.mrbest2525.magicalmechanics.util.math.MMLong;
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

public class FEInputAdapterBlockEntity extends BlockEntity implements IWirelessLinkableTarget, IEnergyStorage, ISourceTypeProvider {
    
    private BlockPos linkedMachineFramePos;
    
    private final MMLong calcBuffer = new MMLong();
    private final MMLong energyBuffer = new MMLong();
    
    public FEInputAdapterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FE_INPUT_ADAPTER.get(), pos, blockState);
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
        if (maxReceive <= 0 || linkedMachineFramePos == null || level == null) return 0;
        if (!level.isLoaded(linkedMachineFramePos)) return 0;
        
        BlockEntity blockEntity = level.getBlockEntity(linkedMachineFramePos);
        if (!(blockEntity instanceof MachineFrameBlockEntity machineFrame)) return 0;
        
        CoreInstance coreInstance = machineFrame.getCoreInstance();
        if (coreInstance == null || !coreInstance.supportsEnergy()) return 0;
        
        // 外部からの int (FE) を MMLong 用のバッファにセット
        energyBuffer.set(maxReceive);
        
        // CoreInstance 側の addEnergy を利用する。
        // このメソッドは内部で適切に「空き容量」を計算し、実際に受け入れた量を calcBuffer に書き込んでくれる。
        return coreInstance.addEnergy(calcBuffer, energyBuffer, simulate).asInt();
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }
    
    @Override
    public int getEnergyStored() {
        if (linkedMachineFramePos == null || level == null || !level.isLoaded(linkedMachineFramePos)) return 0;
        BlockEntity be = level.getBlockEntity(linkedMachineFramePos);
        if (be instanceof MachineFrameBlockEntity mf && mf.getCoreInstance() != null) {
            // asInt() は MMLong 内で 0 以下のガードが入っているので安全
            return mf.getCoreInstance().getEnergy(energyBuffer).asInt();
        }
        return 0;
    }
    
    @Override
    public int getMaxEnergyStored() {
        if (linkedMachineFramePos == null || level == null || !level.isLoaded(linkedMachineFramePos)) return 0;
        BlockEntity be = level.getBlockEntity(linkedMachineFramePos);
        if (be instanceof MachineFrameBlockEntity mf && mf.getCoreInstance() != null) {
            return mf.getCoreInstance().getMaxEnergy(energyBuffer).asInt();
        }
        return 0;
    }
    
    @Override
    public boolean canExtract() {
        return false;
    }
    
    @Override
    public boolean canReceive() {
        return true;
    }
    
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // FE_INPUT_ADAPTER は自身の BlockEntityType
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK, // エネルギーの Capability を
                ModBlockEntities.FE_INPUT_ADAPTER.get(), // この BE 型に対して登録
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
    
    // ==========================================
    // ⚙️ ISourceTypeProviderの実装
    // ==========================================
    
    
    @Override
    public SourceType getSourceType() {
        return SourceType.MagicalFlux;
    }
}
