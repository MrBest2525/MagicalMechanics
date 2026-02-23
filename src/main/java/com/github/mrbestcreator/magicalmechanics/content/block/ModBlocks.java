package com.github.mrbestcreator.magicalmechanics.content.block;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.FrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MagicalMechanics.MODID);
    
    public static final DeferredBlock<Block> MACHINE_FRAME =
            BLOCKS.register(
                    "machine_frame",
                    () -> new FrameBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.METAL)
                                    .noOcclusion()
                                    .isViewBlocking((state, level, pos) -> false)
                                    .isSuffocating((state, level, pos) -> false)
                    )
            );
    
    private ModBlocks() {}
    
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
