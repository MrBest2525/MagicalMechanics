package com.github.mrbestcreator.magicalmechanics.content.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.wrench.WrenchData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItemDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MagicalMechanics.MODID);
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WrenchData>> WRENCH_DATA =
            COMPONENTS.register("wrench_data", () -> DataComponentType.<WrenchData>builder()
                    .persistent(WrenchData.CODEC)
                    .build());
    
    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
