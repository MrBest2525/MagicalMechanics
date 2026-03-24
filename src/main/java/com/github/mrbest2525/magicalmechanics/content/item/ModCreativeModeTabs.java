package com.github.mrbest2525.magicalmechanics.content.item;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import com.github.mrbest2525.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchMode;
import com.github.mrbest2525.magicalmechanics.util.MMLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicalMechanics.MODID);
    
    // マシーンパーツタブ
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB =
            CREATIVE_MODE_TABS.register("machine_parts_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable(MMLang.ItemGroup.MagicalMechanics.MACHINE_PARTS))
                    .icon(() -> new ItemStack(ModBlockItems.MACHINE_FRAME_BLOCK_ITEMS.get(MachineFrameTiers.STONE.getMachineFrameId()).get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (DeferredItem<Item> item : ModItems.MACHINE_CORE_PARTS.values()) {
                            output.accept(item.get());
                        }
                        for (DeferredItem<Item> item : ModItems.MACHINE_SIDE_PARTS.values()) {
                            output.accept(item.get());
                        }
                    })
                    .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_TAB =
            CREATIVE_MODE_TABS.register("tools_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable(MMLang.ItemGroup.MagicalMechanics.TOOLS))
                    .icon(() -> new ItemStack(ModItems.DIAMOND_MAYURANT.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.WRENCH);
                        ItemStack coreModeWrench = new ItemStack(ModItems.WRENCH.get());
                        if (coreModeWrench.getItem() instanceof WrenchItem wrenchItem) {
                            wrenchItem.setMode(coreModeWrench, WrenchMode.CORE);
                            output.accept(coreModeWrench);
                        }
                        // Mayurantのクリエイティブタブ登録
                        for (DeferredItem<Item> mayurantItem: ModItems.MAYURANT_ITEMS.values()) {
                            output.accept(mayurantItem);
                            ItemStack fullMayurant = new ItemStack(mayurantItem.get());
                            if (fullMayurant.getItem() instanceof MayurantItem mayurant) {
                                mayurant.setMagicPower(fullMayurant, mayurant.getMaxMagicPower());
                                output.accept(fullMayurant);
                            }
                        }
                    }))
                    .build());
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINE_FRAMES_TAB =
            CREATIVE_MODE_TABS.register("machine_frames_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable(MMLang.ItemGroup.MagicalMechanics.MACHINE_FRAMES))
                    .icon(() -> new ItemStack(ModBlockItems.MACHINE_FRAME_BLOCK_ITEMS.get(MachineFrameTiers.STONE.getMachineFrameId()).get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlockItems.BASE_FRAME_ITEM);
                        
                        // MachineFrames
                        for (MachineFrameTiers tier : MachineFrameTiers.values()) {
                            var itemHolder = ModBlockItems.MACHINE_FRAME_BLOCK_ITEMS.get(tier.getMachineFrameId());
                            if (itemHolder != null) {
                                output.accept(itemHolder.get());
                            }
                        }
                    })
                    .build());
    
    
    private ModCreativeModeTabs() {}
    
    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
