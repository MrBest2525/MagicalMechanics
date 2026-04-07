package com.github.mrbest2525.magicalmechanics.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum SourceType implements StringRepresentable {
    // 電力
    MagicalFlux("magical_flux"),
    // 機械での魔力
    ViservalFlux("viserval_flux"),
    // Playerや自然界の魔力
    Mana("mana"),
    // 熱
    Thermal("thermal");
    
    // StringRepresentable のための実装
    private final String name;
    SourceType(String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
    
    // Codec の定義（これで DataComponent から参照可能になる）
    public static final Codec<SourceType> CODEC = StringRepresentable.fromEnum(SourceType::values);
    public static final StreamCodec<ByteBuf, SourceType> STREAM_CODEC =
            ByteBufCodecs.idMapper(id -> SourceType.values()[id], SourceType::ordinal);
}
