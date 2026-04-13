package com.github.mrbest2525.magicalmechanics.content.item.wrench;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum WrenchMode implements StringRepresentable {
    SIDE("side"), CORE("core");
    
    private final String name;
    WrenchMode(String name) { this.name = name; }
    
    @Override
    public @NotNull String getSerializedName() { return this.name; }
    
    public static final Codec<WrenchMode> CODEC =
            StringRepresentable.fromEnum(WrenchMode::values);
    
    public static final StreamCodec<ByteBuf, WrenchMode> STREAM_CODEC =
            ByteBufCodecs.idMapper(i -> WrenchMode.values()[i], WrenchMode::ordinal);
}
