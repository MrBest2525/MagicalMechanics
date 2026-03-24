package com.github.mrbest2525.magicalmechanics.content.block;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.IMachineFrameTier;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlock;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.base.BaseFrameBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MagicalMechanics.MODID);
    
    public static final DeferredBlock<Block> BASE_FRAME =
            BLOCKS.register(
                    "base_frame",
                    () -> new BaseFrameBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(0.0f, 15f)
                                    .sound(SoundType.WOOD)
                                    .mapColor(MapColor.WOOD)
                                    .noOcclusion()
                                    .isViewBlocking((state, level, pos) -> false)
                                    .isSuffocating((state, level, pos) -> false)
                                    .lightLevel((state) -> 1)
                            )
            );
    
    public static final DeferredBlock<Block> MACHINE_FRAME =
            BLOCKS.register("machine_frame", () -> new MachineFrameBlock(MachineFrameTiers.IRON));
    
    public static final Map<String, DeferredBlock<Block>> MACHINE_FRAMES = new HashMap<>();
    static {
        for (IMachineFrameTier tier: MachineFrameTiers.values()) {
            MACHINE_FRAMES.put(tier.getMachineFrameId(), BLOCKS.register(tier.getMachineFrameId(), () -> new MachineFrameBlock(tier)));
        }
    }
    
    private ModBlocks() {}
    
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
