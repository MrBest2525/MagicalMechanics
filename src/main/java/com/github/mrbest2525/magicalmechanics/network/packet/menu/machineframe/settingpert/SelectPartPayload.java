package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SelectPartPayload(BlockPos pos, UUID uuid) implements CustomPacketPayload {
    public static final Type<SelectPartPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "remove_frame_part"));
    
    public static final StreamCodec<ByteBuf, SelectPartPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SelectPartPayload::pos,
            UUIDUtil.STREAM_CODEC, SelectPartPayload::uuid,
            SelectPartPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
