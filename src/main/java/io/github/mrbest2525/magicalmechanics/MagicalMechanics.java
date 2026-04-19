package io.github.mrbest2525.magicalmechanics;

import io.github.mrbest2525.magicalmechanics.client.item.ModColorHandlers;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import io.github.mrbest2525.magicalmechanics.content.block.machine.energy.FEInputAdapterBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.block.machine.energy.FEOutputAdapterBlockEntity;
import io.github.mrbest2525.magicalmechanics.content.item.ModCreativeModeTabs;
import io.github.mrbest2525.magicalmechanics.content.item.ModItemDataComponents;
import io.github.mrbest2525.magicalmechanics.content.item.ModItems;
import io.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import io.github.mrbest2525.magicalmechanics.util.WorldMultiplier;
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
        
        // 電力
        modEventBus.addListener(FEInputAdapterBlockEntity::registerCapabilities);
        modEventBus.addListener(FEOutputAdapterBlockEntity::registerCapabilities);
        
        // テクスチャの色付け
        modEventBus.addListener(ModColorHandlers::registerItemColors);
    }
    
    @SubscribeEvent
    public static void onWorldLoad(ServerAboutToStartEvent event) {
        worldMultiplier = new WorldMultiplier();
        worldMultiplier.initializeRandomly(event.getServer().getWorldData().worldGenOptions().seed());
    }
}