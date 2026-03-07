package com.github.mrbestcreator.magicalmechanics;

import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsScreen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MACHINE_FRAME_MENU.get(), FrameBlockScreen::new);
        event.register(ModMenus.FURNACE_CORE_PARTS_MENU.get(), FurnaceCorePartsScreen::new);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.IRON_MAYURANT.get(), // 登録したアイテム
                    ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "magic_power"), // JSONのpredicateで使う名前
                    (stack, level, entity, seed) -> {
                        // コンポーネントから値を取得
                        int power;
                        if (stack.getItem() instanceof MayurantItem mayurantItem) {
                            power = stack.getOrDefault(ModItemDataComponents.MAGIC_POWER.get(), mayurantItem.getMinMagicPower());
                        } else {
                            power = stack.getOrDefault(ModItemDataComponents.MAGIC_POWER.get(), 0);
                        }
                        // 1以上なら 1.0F（アクティブ）、0なら 0.0F（通常）を返す
                        return power > 0 ? 1.0F : 0.0F;
                    }
            );
        });
    }
}
