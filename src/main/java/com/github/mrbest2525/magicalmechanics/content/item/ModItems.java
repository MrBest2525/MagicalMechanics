package com.github.mrbest2525.magicalmechanics.content.item;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.core.energy.EnergyCoreTiers;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.items.*;
import com.github.mrbest2525.magicalmechanics.content.item.frameparts.instance.side.energy.WirelessEnergySideTiers;
import com.github.mrbest2525.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbest2525.magicalmechanics.content.item.mayurant.MayurantTiers;
import com.github.mrbest2525.magicalmechanics.content.item.mfwirelesslinker.MFLinkedStaffItem;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchData;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbest2525.magicalmechanics.content.item.wrench.WrenchMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MagicalMechanics.MODID);
    
    // MachineCoreParts
    public static final DeferredItem<Item> FURNACE_CORE = ITEMS.register(
            "furnace_core",
            () -> new FurnaceCoreItem(new Item.Properties())
    );
    
    public static final DeferredItem<Item> UNLIMITED_ENERGY_CORE = ITEMS.register("unlimited_energy_core", () -> new EnergyCoreItem(new Item.Properties(), EnergyCoreTiers.UNLIMITED));
    public static final DeferredItem<Item> NORMAL_ENERGY_CORE = ITEMS.register("normal_energy_core", () -> new EnergyCoreItem(new Item.Properties(), EnergyCoreTiers.NORMAL));
    public static final DeferredItem<Item> ADVANCED_ENERGY_CORE = ITEMS.register("advanced_energy_core", () -> new EnergyCoreItem(new Item.Properties(), EnergyCoreTiers.ADVANCED));
    
    public static final Map<String, DeferredItem<Item>> MACHINE_CORE_PARTS = new HashMap<>();
    // ==============================
    // SettingParts
    // ==============================
    public static final DeferredItem<Item> TEST_SETTING_PARTS1 =
            ITEMS.register("test_setting_parts1", () -> new TestSettingPartsItem(new Item.Properties()));
    static {
        for (int i = 0; i < 30; i++) {
            ITEMS.register("test_setting_parts" + (i + 3), () -> new TestSettingPartsItem(new Item.Properties()));
        }
    }
    
    
    // MachineSideParts
    public static final DeferredItem<Item> FURNACE_SIDE = ITEMS.register(
            "furnace_side",
            () -> new FurnaceSideItem(new Item.Properties())
    );
    
    public static final DeferredItem<Item> UNLIMITED_WIRELESS_ENERGY_INTERFACE =
            ITEMS.register("unlimited_wireless_energy_interface", () -> new WirelessEnergyInterfaceSideItem(new Item.Properties(), WirelessEnergySideTiers.UNLIMITED));
    
    public static final Map<String, DeferredItem<Item>> MACHINE_SIDE_PARTS = new HashMap<>();
    static {
        MACHINE_SIDE_PARTS.put(FURNACE_SIDE.getRegisteredName(), FURNACE_SIDE);
        MACHINE_SIDE_PARTS.put(UNLIMITED_WIRELESS_ENERGY_INTERFACE.getRegisteredName(), UNLIMITED_WIRELESS_ENERGY_INTERFACE);
    }
    public static final DeferredItem<Item> TEST_SETTING_PARTS2 =
            ITEMS.register("test_setting_parts2", () -> new TestSettingPartsItem(new Item.Properties()));

    static {
        MACHINE_CORE_PARTS.put(FURNACE_CORE.getRegisteredName(), FURNACE_CORE);
        MACHINE_CORE_PARTS.put(UNLIMITED_ENERGY_CORE.getRegisteredName(), UNLIMITED_ENERGY_CORE);
        MACHINE_CORE_PARTS.put(NORMAL_ENERGY_CORE.getRegisteredName(), NORMAL_ENERGY_CORE);
        MACHINE_CORE_PARTS.put(ADVANCED_ENERGY_CORE.getRegisteredName(), ADVANCED_ENERGY_CORE);
    }
    
    
    // Wrench
    public static final DeferredItem<Item> WRENCH = ITEMS.register("wrench", () -> new WrenchItem(
            new Item.Properties()
                    .component(ModItemDataComponents.WRENCH_DATA, new WrenchData(WrenchMode.SIDE))
                    .stacksTo(1)
    ));
    
    public static final Map<String, DeferredItem<Item>> WRENCH_ITEMS = new HashMap<>();
    static {
        WRENCH_ITEMS.put(WRENCH.getRegisteredName(), WRENCH);
    }
    
    
    
    // Mayurant
    public static final DeferredItem<Item> WOODEN_MAYURANT = ITEMS.register(MayurantTiers.WOOD.getId(), () -> new MayurantItem(MayurantTiers.WOOD, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.WOOD, 1.0f, -2.8f))));
    public static final DeferredItem<Item> STONE_MAYURANT = ITEMS.register(MayurantTiers.STONE.getId(), () -> new MayurantItem(MayurantTiers.STONE, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.STONE, 1.0f, -2.8f))));
    public static final DeferredItem<Item> IRON_MAYURANT = ITEMS.register(MayurantTiers.IRON.getId(), () -> new MayurantItem(MayurantTiers.IRON, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.IRON, 1.0f, -2.8f))));
    public static final DeferredItem<Item> GOLDEN_MAYURANT = ITEMS.register(MayurantTiers.GOLD.getId(), () -> new MayurantItem(MayurantTiers.GOLD, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.GOLD, 1.0f, -2.8f))));
    public static final DeferredItem<Item> DIAMOND_MAYURANT = ITEMS.register(MayurantTiers.DIAMOND.getId(), () -> new MayurantItem(MayurantTiers.DIAMOND, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.DIAMOND, 1.0f, -2.8f))));
    public static final DeferredItem<Item> NETHERITE_MAYURANT = ITEMS.register(MayurantTiers.NETHERITE.getId(), () -> new MayurantItem(MayurantTiers.NETHERITE, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.NETHERITE, 1.0f, -2.8f))));
    
    public static final Map<String, DeferredItem<Item>> MAYURANT_ITEMS = new HashMap<>();
    static {
        MAYURANT_ITEMS.put(MayurantTiers.WOOD.getId(), WOODEN_MAYURANT);
        MAYURANT_ITEMS.put(MayurantTiers.STONE.getId(), STONE_MAYURANT);
        MAYURANT_ITEMS.put(MayurantTiers.IRON.getId(), IRON_MAYURANT);
        MAYURANT_ITEMS.put(MayurantTiers.GOLD.getId(), GOLDEN_MAYURANT);
        MAYURANT_ITEMS.put(MayurantTiers.DIAMOND.getId(), DIAMOND_MAYURANT);
        MAYURANT_ITEMS.put(MayurantTiers.NETHERITE.getId(), NETHERITE_MAYURANT);
    }
    
    public static final DeferredItem<Item> MF_LINK_STAFF = ITEMS.register("mf_link_staff", () -> new MFLinkedStaffItem(
            new Item.Properties()
                    .stacksTo(1)
    ));
    
    
    
    private ModItems() {}
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
