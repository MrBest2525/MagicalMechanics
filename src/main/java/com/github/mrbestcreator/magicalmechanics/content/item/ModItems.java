package com.github.mrbestcreator.magicalmechanics.content.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.parts.FurnaceCoreItem;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchData;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchItem;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchMode;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

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
    
    private ModItems() {}
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
