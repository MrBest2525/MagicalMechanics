package io.github.mrbest2525.magicalmechanics;

import io.github.mrbest2525.magicalmechanics.client.block.ber.FEAdapterBlockBER;
import io.github.mrbest2525.magicalmechanics.client.block.ber.MachineFrameBER;
import io.github.mrbest2525.magicalmechanics.client.item.bewlr.MFLinkStaffItemRenderer;
import io.github.mrbest2525.magicalmechanics.client.item.bewlr.MachineFrameItemRenderer;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockEntities;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import io.github.mrbest2525.magicalmechanics.content.item.ModItemDataComponents;
import io.github.mrbest2525.magicalmechanics.content.item.ModItems;
import io.github.mrbest2525.magicalmechanics.content.item.mayurant.MayurantItem;
import io.github.mrbest2525.magicalmechanics.content.menu.ModMenus;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockScreen;
import io.github.mrbest2525.magicalmechanics.content.menu.block.machine.frame.FrameBlockSettingPartsScreen;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.energycore.EnergyCorePartScreen;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.furnacecore.FurnaceCorePartsScreen;
import io.github.mrbest2525.magicalmechanics.content.menu.item.machineparts.furnaceside.FurnaceSidePartsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = MagicalMechanics.MODID, value = Dist.CLIENT)
public class ClientSetup {
    
    // インスタンスを保持する変数（シングルトン用）
    private static MachineFrameItemRenderer MACHINE_FRAME_RENDERER;
    private static MFLinkStaffItemRenderer MF_LINK_STAFF_RENDERER;
    
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MACHINE_FRAME_MENU.get(), FrameBlockScreen::new);
        event.register(ModMenus.MACHINE_FRAME_SETTING_PARTS_MENU.get(), FrameBlockSettingPartsScreen::new);
        event.register(ModMenus.FURNACE_CORE_PARTS_MENU.get(), FurnaceCorePartsScreen::new);
        event.register(ModMenus.FURNACE_SIDE_PARTS_MENU.get(), FurnaceSidePartsScreen::new);
        event.register(ModMenus.ENERGY_CORE_PART_MENU.get(), EnergyCorePartScreen::new);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (DeferredItem<Item> mayurant: ModItems.MAYURANT_ITEMS.values()) {
            event.enqueueWork(() -> ItemProperties.register(
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
            ));
        }
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MACHINE_FRAME.get(), MachineFrameBER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FE_INPUT_ADAPTER.get(), FEAdapterBlockBER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FE_OUTPUT_ADAPTER.get(), FEAdapterBlockBER::new);
    }
    
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        // 1. レンダラーの遅延初期化（すでにある場合はそれを使う）
        // ※クラス内で private static で保持している前提
        if (MACHINE_FRAME_RENDERER == null) {
            Minecraft mc = Minecraft.getInstance();
            MACHINE_FRAME_RENDERER = new MachineFrameItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
            MF_LINK_STAFF_RENDERER = new MFLinkStaffItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
        }
        
        // 2. IClientItemExtensions の定義
        IClientItemExtensions machineFrameExt = new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return MACHINE_FRAME_RENDERER;
            }
        };
        
        IClientItemExtensions staffExt = new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return MF_LINK_STAFF_RENDERER;
            }
        };
        
        // 3. 登録
        ModBlocks.MACHINE_FRAMES.values().forEach(blockHolder -> event.registerItem(machineFrameExt, blockHolder.asItem()));
        event.registerItem(machineFrameExt, ModBlockItems.MACHINE_FRAME_BLOCK_ITEM.get());
        event.registerItem(staffExt, ModItems.MF_LINK_STAFF.get());
    }
    
    @SubscribeEvent
    public static void onAdditionalModels(ModelEvent.RegisterAdditional event) {
        // 描画コード内で呼び出したい各 JSON モデルのパスを登録する
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_base"), "standalone"));
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_orb_core"), "standalone"));
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/mf_link_staff_orb_aura"), "standalone"));
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "orb/orb_core"), "standalone"));
        event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "orb/orb_aura"), "standalone"));
    }
}
