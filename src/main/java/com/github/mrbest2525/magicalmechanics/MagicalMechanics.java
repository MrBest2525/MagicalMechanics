package com.github.mrbest2525.magicalmechanics;

import com.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import com.github.mrbest2525.magicalmechanics.content.item.ModCreativeModeTabs;
import com.github.mrbest2525.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import com.github.mrbest2525.magicalmechanics.util.WorldMultiplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Mod(MagicalMechanics.MODID)
public class MagicalMechanics {
    
    public static final String MODID = "magicalmechanics";
    
    public static WorldMultiplier worldMultiplier;
    
    public MagicalMechanics(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlockItems.register(modEventBus);
        ModItemDataComponents.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMenus.register(modEventBus);
    }
    
    @SubscribeEvent
    public static void onWorldLoad(ServerAboutToStartEvent event) {
        worldMultiplier = new WorldMultiplier();
        worldMultiplier.initializeRandomly(event.getServer().getWorldData().worldGenOptions().seed());
    }
}