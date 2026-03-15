package com.github.mrbestcreator.magicalmechanics.content.menu;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsMenu;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside.FurnaceSidePartsMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MagicalMechanics.MODID);
    
    public static final Supplier<MenuType<FrameBlockMenu>> MACHINE_FRAME_MENU =
            MENUS.register("machine_frame_menu", () -> IMenuTypeExtension.create(FrameBlockMenu::new));
    public static final Supplier<MenuType<FurnaceCorePartsMenu>> FURNACE_CORE_PARTS_MENU =
            MENUS.register("furnace_core_parts_menu", () -> IMenuTypeExtension.create(FurnaceCorePartsMenu::new));
    public static final Supplier<MenuType<FurnaceSidePartsMenu>> FURNACE_SIDE_PARTS_MENU =
            MENUS.register("furnace_side_parts_menu", () -> IMenuTypeExtension.create(FurnaceSidePartsMenu::new));
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
