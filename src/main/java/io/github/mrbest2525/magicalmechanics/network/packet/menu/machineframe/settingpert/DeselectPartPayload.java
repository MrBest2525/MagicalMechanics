package io.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record DeselectPartPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<DeselectPartPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "install_part"));
    
    public static final StreamCodec<ByteBuf, DeselectPartPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, DeselectPartPayload::pos,
            DeselectPartPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
