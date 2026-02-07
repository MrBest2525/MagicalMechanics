package com.github.mrbestcreator.magicalmechanics.content.block;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS =
            DeferredRegister.createItems(MagicalMechanics.MODID);
    
    public static final DeferredItem<BlockItem> MACHINE_FRAME_BLOCK_ITEM =
            BLOCK_ITEMS.registerSimpleBlockItem("machine_frame", ModBlocks.MACHINE_FRAME);
    
    private ModBlockItems() {}
    
    public static void register(IEventBus bus) {
        BLOCK_ITEMS.register(bus);
    }
}
