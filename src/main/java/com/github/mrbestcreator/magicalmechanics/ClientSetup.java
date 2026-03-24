package com.github.mrbestcreator.magicalmechanics;

import com.github.mrbestcreator.magicalmechanics.client.block.ber.MachineFrameBER;
import com.github.mrbestcreator.magicalmechanics.client.item.bewlr.MachineFrameItemRenderer;
import com.github.mrbestcreator.magicalmechanics.client.item.bewlr.ModClientExtensions;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantItem;
import com.github.mrbestcreator.magicalmechanics.content.menu.ModMenus;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsScreen;
import com.github.mrbestcreator.magicalmechanics.content.menu.item.machineparts.furnaceside.FurnaceSidePartsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ClientSetup {
    
    // インスタンスを保持する変数（シングルトン用）
    private static MachineFrameItemRenderer MACHINE_FRAME_RENDERER;
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MACHINE_FRAME_MENU.get(), FrameBlockScreen::new);
        event.register(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), FrameBlockSettingPartsScreen::new);
        event.register(ModMenus.FURNACE_CORE_PARTS_MENU.get(), FurnaceCorePartsScreen::new);
        event.register(ModMenus.FURNACE_SIDE_PARTS_MENU.get(), FurnaceSidePartsScreen::new);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (DeferredItem<Item> mayurant: ModItems.MAYURANT_ITEMS.values()) {
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
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MACHINE_FRAME.get(), MachineFrameBER::new);
    }
    
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        
        Minecraft mc = Minecraft.getInstance();
        MACHINE_FRAME_RENDERER = new MachineFrameItemRenderer(
                mc.getBlockEntityRenderDispatcher(),
                mc.getEntityModels()
        );
        
        // MACHINE_FRAMES Map内のすべてのアイテムにレンダラーを登録
        ModBlocks.MACHINE_FRAMES.values().forEach(blockHolder -> {
            event.registerItem(new IClientItemExtensions() {
                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    // ここで作成したRendererのインスタンスを返す
                    // 必要に応じてシングルトンとして保持しておくと効率的です
                    return MACHINE_FRAME_RENDERER;
                }
            }, blockHolder.asItem());
        });
        
        // ここで特定のアイテムに対して、レンダラー（拡張）を紐付ける
        event.registerItem(
                new IClientItemExtensions() {
                    @Override
                    public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return ModClientExtensions.getMachineFrameItemRenderer();
                    }
                },
                ModBlockItems.MACHINE_FRAME_BLOCK_ITEM.get() // 対象のアイテム
        );
    }
}
