package com.github.mrbestcreator.magicalmechanics.content.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.parts.FurnaceCoreItem;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantTiers;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchData;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MagicalMechanics.MODID);
    
    public static final DeferredItem<Item> FURNACE_CORE = ITEMS.register(
            "furnace_core",
            () -> new FurnaceCoreItem(new Item.Properties())
    );
    
    public static final DeferredItem<Item> WRENCH = ITEMS.register("wrench", () -> new WrenchItem(
            new Item.Properties()
                    .component(ModItemDataComponents.WRENCH_DATA, new WrenchData(WrenchMode.SIDE))
                    .stacksTo(1)
    ));
    
    
    // Mayurant
    public static final DeferredItem<Item> WOODEN_MAYURANT = ITEMS.register("wooden_mayurant", () -> new MayurantItem(MayurantTiers.WOOD, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.WOOD, 1.0f, -2.8f))));
    public static final DeferredItem<Item> STONE_MAYURANT = ITEMS.register("stone_mayurant", () -> new MayurantItem(MayurantTiers.STONE, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.STONE, 1.0f, -2.8f))));
    public static final DeferredItem<Item> IRON_MAYURANT = ITEMS.register("iron_mayurant", () -> new MayurantItem(MayurantTiers.IRON, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.IRON, 1.0f, -2.8f))));
    public static final DeferredItem<Item> GOLDEN_MAYURANT = ITEMS.register("golden_mayurant", () -> new MayurantItem(MayurantTiers.GOLD, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.GOLD, 1.0f, -2.8f))));
    public static final DeferredItem<Item> DIAMOND_MAYURANT = ITEMS.register("diamond_mayurant", () -> new MayurantItem(MayurantTiers.DIAMOND, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.DIAMOND, 1.0f, -2.8f))));
    public static final DeferredItem<Item> NETHERITE_MAYURANT = ITEMS.register("netherite_mayurant", () -> new MayurantItem(MayurantTiers.NETHERITE, new Item.Properties().component(ModItemDataComponents.MAGIC_POWER, 0).stacksTo(1).component(DataComponents.ATTRIBUTE_MODIFIERS, MayurantItem.createAttributes(MayurantTiers.NETHERITE, 1.0f, -2.8f))));
    
    public static final List<DeferredItem<Item>> MAYURANT_ITEMS = new ArrayList<>();
    static {
        MAYURANT_ITEMS.add(WOODEN_MAYURANT);
        MAYURANT_ITEMS.add(STONE_MAYURANT);
        MAYURANT_ITEMS.add(IRON_MAYURANT);
        MAYURANT_ITEMS.add(GOLDEN_MAYURANT);
        MAYURANT_ITEMS.add(DIAMOND_MAYURANT);
        MAYURANT_ITEMS.add(NETHERITE_MAYURANT);
    }
    
    private ModItems() {}
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
