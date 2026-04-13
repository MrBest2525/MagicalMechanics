package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.settingpert;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.SettingPartsManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SyncFramePartsPayload(BlockPos pos, Map<UUID, SettingPartsManager.PartItemStack> parts) implements CustomPacketPayload {
    public static final Type<SyncFramePartsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "sync_frame_parts"));
    
    // UUIDとPartItemStackのMapを読み書きするためのCodec
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncFramePartsPayload> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncFramePartsPayload::pos,
            // Map<UUID, PartItemStack> のシリアライズ
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, SettingPartsManager.PartItemStack.STREAM_CODEC), SyncFramePartsPayload::parts,
            SyncFramePartsPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
