package com.github.mrbestcreator.magicalmechanics;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.item.ModCreativeModeTabs;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MagicalMechanics.MODID)
public class MagicalMechanics {
    
    public static final String MODID = "magicalmechanics";
    
    public MagicalMechanics(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlockItems.register(modEventBus);
        ModItemDataComponents.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
    }
}