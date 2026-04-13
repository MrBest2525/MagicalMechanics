package com.github.mrbest2525.magicalmechanics.content.block;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.machine.energy.FEInputAdapterBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.machine.energy.FEOutputAdapterBlockEntity;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MagicalMechanics.MODID);
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MachineFrameBlockEntity>> MACHINE_FRAME =
            BLOCK_ENTITIES.register(
                    "machine_frame",
                    () -> BlockEntityType.Builder.of(
                            MachineFrameBlockEntity::new,
                            // 新旧すべてのブロックを一つのリストにまとめる
                            Stream.concat(
                                    ModBlocks.MACHINE_FRAMES.values().stream().map(DeferredBlock::get), // 新ティア（Map）
                                    Stream.of(ModBlocks.MACHINE_FRAME.get()) // 旧フレーム
                            ).toArray(Block[]::new)
                    ).build(null)
            );
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FEInputAdapterBlockEntity>> FE_INPUT_ADAPTER =
            BLOCK_ENTITIES.register(
                    "fe_input_adapter",
                    () -> BlockEntityType.Builder.of(
                            FEInputAdapterBlockEntity::new,
                            ModBlocks.FE_INPUT_ADAPTER_BLOCKS.values().stream().map(DeferredBlock::get).toArray(Block[]::new)
                    ).build(null)
            );
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FEOutputAdapterBlockEntity>> FE_OUTPUT_ADAPTER =
            BLOCK_ENTITIES.register(
                    "fe_output_adapter",
                    () -> BlockEntityType.Builder.of(
                            FEOutputAdapterBlockEntity::new,
                            ModBlocks.FE_OUTPUT_ADAPTER_BLOCKS.values().stream().map(DeferredBlock::get).toArray(Block[]::new)
                    ).build(null)
            );
    
    private ModBlockEntities() {}
    
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
