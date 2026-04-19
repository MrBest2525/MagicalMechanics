package io.github.mrbest2525.magicalmechanics.content.item.wrench;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WrenchData(WrenchMode mode) {
    public static final Codec<WrenchData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
//                    Codec.STRING.fieldOf("mode").forGetter(WrenchData::mode)
                    WrenchMode.CODEC.fieldOf("mode").forGetter(WrenchData::mode)
            ).apply(instance, WrenchData::new)
    );
}
