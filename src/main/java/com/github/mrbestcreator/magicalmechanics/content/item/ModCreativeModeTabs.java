package com.github.mrbestcreator.magicalmechanics.content.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicalMechanics.MODID);
    
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB =
            CREATIVE_MODE_TABS.register("machines_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.magicalmechanics.machines"))
                    .icon(() -> new ItemStack(ModBlockItems.MACHINE_FRAME_BLOCK_ITEM.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlockItems.MACHINE_FRAME_BLOCK_ITEM);
                    })
                    .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_TAB =
            CREATIVE_MODE_TABS.register("tools_Tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.magicalmechanics.tools"))
                    .icon(() -> new ItemStack(ModItems.WRENCH.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WRENCH);
                    }))
                    .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINE_FRAMES_TAB =
            CREATIVE_MODE_TABS.register("machine_frame_Tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.magicalmechanics.machineFrames"))
                    .icon(() -> new ItemStack(ModBlockItems.MACHINE_FRAME_BLOCK_ITEM.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.FURNACE_CORE);
                    })
                    .build());
    
    
    private ModCreativeModeTabs() {}
    
    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
