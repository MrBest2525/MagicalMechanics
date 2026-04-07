package com.github.mrbest2525.magicalmechanics.content.item;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.api.SourceType;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchData;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItemDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MagicalMechanics.MODID);
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WrenchData>> WRENCH_DATA =
            COMPONENTS.register("wrench_data", () -> DataComponentType.<WrenchData>builder()
                    .persistent(WrenchData.CODEC)
                    .build());
    
    // ツール等に込めれる魔力の単位はMagicPowerとする
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAGIC_POWER =
            COMPONENTS.register("magic_power", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());
    
    // Linkerのリンク先情報保存
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> LINKED_BLOCK_POS =
            COMPONENTS.register("linked_block_pos", () -> DataComponentType.<BlockPos>builder()
                    .persistent(BlockPos.CODEC)
                    .networkSynchronized(BlockPos.STREAM_CODEC)
                    .build());
    
    // LinkerのSourceTypeの保持
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SourceType>> LINKED_SOURCE_TYPE =
            COMPONENTS.register("linked_source_type", () -> DataComponentType.<SourceType>builder()
                    .persistent(SourceType.CODEC)
                    .networkSynchronized(SourceType.STREAM_CODEC)
                    .build());
    
    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
