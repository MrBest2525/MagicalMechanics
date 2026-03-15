package com.github.mrbestcreator.magicalmechanics.content.block;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MagicalMechanics.MODID);
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MachineFrameBlockEntity>> MACHINE_FRAME =
            BLOCK_ENTITIES.register(
                    "machine_frame",
                    () -> BlockEntityType.Builder.of(
                            com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameBlockEntity::new,
                            ModBlocks.MACHINE_FRAME.get()
                    ).build(null)
            );
    
    private ModBlockEntities() {}
    
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
