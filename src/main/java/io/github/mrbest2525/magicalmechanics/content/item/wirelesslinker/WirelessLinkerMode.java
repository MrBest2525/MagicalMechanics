package io.github.mrbest2525.magicalmechanics.content.item.wirelesslinker;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum WirelessLinkerMode implements StringRepresentable {
    SOURCE("source"),
    TARGET("target");
    
    private final String name;
    
    WirelessLinkerMode(String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
    
    // 保存・読み込み用の Codec
    public static final Codec<WirelessLinkerMode> CODEC =
            StringRepresentable.fromEnum(WirelessLinkerMode::values);
    
    // 保存用の CODEC をネットワーク用(ByteBuf)に変換して再利用します
    public static final StreamCodec<FriendlyByteBuf, WirelessLinkerMode> STREAM_CODEC =
            StreamCodec.of(
                    FriendlyByteBuf::writeEnum, // 書き込み
                    buf -> buf.readEnum(WirelessLinkerMode.class) // 読み込み
            );
}
