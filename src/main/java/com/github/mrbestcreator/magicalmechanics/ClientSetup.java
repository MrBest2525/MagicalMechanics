package com.github.mrbestcreator.magicalmechanics;

import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsScreen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredItem;

@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MACHINE_FRAME_MENU.get(), FrameBlockScreen::new);
        event.register(ModMenus.FURNACE_CORE_PARTS_MENU.get(), FurnaceCorePartsScreen::new);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (DeferredItem<Item> mayurant: ModItems.MAYURANT_ITEMS) {
            event.enqueueWork(() -> {
                ItemProperties.register(
                        mayurant.get(), // 登録したアイテム
                        ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "magic_power"), // JSONのpredicateで使う名前
                        (stack, level, entity, seed) -> {
                            // コンポーネントから値を取得
                            int power;
                            int minPower = 0;
                            if (stack.getItem() instanceof MayurantItem mayurantItem) {
                                power = stack.getOrDefault(ModItemDataComponents.MAGIC_POWER.get(), mayurantItem.getMinMagicPower());
                                minPower = mayurantItem.getMinMagicPower();
                            } else {
                                power = stack.getOrDefault(ModItemDataComponents.MAGIC_POWER.get(), 0);
                            }
                            // MagicPowerが空でないなら1.0Fを返す
                            return power > minPower ? 1.0F : 0.0F;
                        }
                );
            });
        }
    }
}
