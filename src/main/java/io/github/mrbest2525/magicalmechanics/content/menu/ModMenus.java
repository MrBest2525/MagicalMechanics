package io.github.mrbest2525.magicalmechanics.content.menu;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsMenu;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore.EnergyCorePartMenu;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsMenu;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.furnaceside.FurnaceSidePartsMenu;
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
    public static final Supplier<MenuType<FrameBlockSettingPartsMenu>> MACHINE_FRAME_SETTING_PARTS_MENU =
            MENUS.register("machine_frame_setting_parts_menu", () -> IMenuTypeExtension.create(FrameBlockSettingPartsMenu::new));
    
    public static final Supplier<MenuType<FurnaceCorePartsMenu>> FURNACE_CORE_PARTS_MENU =
            MENUS.register("furnace_core_parts_menu", () -> IMenuTypeExtension.create(FurnaceCorePartsMenu::new));
    public static final Supplier<MenuType<FurnaceSidePartsMenu>> FURNACE_SIDE_PARTS_MENU =
            MENUS.register("furnace_side_parts_menu", () -> IMenuTypeExtension.create(FurnaceSidePartsMenu::new));
    
    public static final Supplier<MenuType<EnergyCorePartMenu>> ENERGY_CORE_PART_MENU =
            MENUS.register("energy_core_part_menu", () -> IMenuTypeExtension.create(EnergyCorePartMenu::new));
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
