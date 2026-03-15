package com.github.mrbestcreator.magicalmechanics.network.packet.menu;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record OpenFramePartPayload(BlockPos pos, int partId) implements CustomPacketPayload {
    public static final Type<OpenFramePartPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "open_frame_part"));
    
    // ネットワーク経由で読み書きするためのCodec
    public static final StreamCodec<ByteBuf, OpenFramePartPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenFramePartPayload::pos,
            ByteBufCodecs.VAR_INT, OpenFramePartPayload::partId,
            OpenFramePartPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
