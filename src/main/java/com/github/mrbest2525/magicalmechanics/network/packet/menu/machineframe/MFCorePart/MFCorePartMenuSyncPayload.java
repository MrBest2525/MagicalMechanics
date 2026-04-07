package com.github.mrbest2525.magicalmechanics.network.packet.menu.machineframe.MFCorePart;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record MFCorePartMenuSyncPayload(
        byte mode,             // 0: 正確, 1: 指数, 2: 無制限
        long currentRaw,       // 正確モード用
        long maxRaw,           // 正確モード用
        int currentMantissa,   // 指数モード用 (例: 1444)
        int currentExponent,   // 指数モード用 (例: 100)
        int maxMantissa,       // 追加
        int maxExponent
) implements CustomPacketPayload {
    
    public static final Type<MFCorePartMenuSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "mf_menu_sync"));
    
    public static final StreamCodec<FriendlyByteBuf, MFCorePartMenuSyncPayload> CODEC = StreamCodec.of(
            (buf, payload) -> {
                // 書き込み（encode）
                buf.writeByte(payload.mode());
                buf.writeVarLong(payload.currentRaw());
                buf.writeVarLong(payload.maxRaw());
                buf.writeVarInt(payload.currentMantissa());
                buf.writeVarInt(payload.currentExponent());
                buf.writeVarInt(payload.maxMantissa());
                buf.writeVarInt(payload.maxExponent());
            },
            (buf) -> {
                // 読み込み（decode）- レコードのコンストラクタ順に合わせる
                return new MFCorePartMenuSyncPayload(
                        buf.readByte(),
                        buf.readVarLong(),
                        buf.readVarLong(),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readVarInt()
                );
            }
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
