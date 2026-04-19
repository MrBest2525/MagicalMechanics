package io.github.mrbest2525.magicalmechanics.datagen.client.item.model.item;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.registries.DeferredItem;

public class MayurantItemModelGenerator {
    private final ItemModelProvider provider;
    
    public MayurantItemModelGenerator(ItemModelProvider provider) {
        this.provider = provider;
    }
    
    /**
     * マユラント専用のモデル生成
     */
    public void generateMayurantModel(DeferredItem<?> item) {
        // 登録名（例: "iron_mayurant"）を取得
        String name = item.getId().getPath();
        
        ItemModelBuilder magicPowerActiveModel = provider.withExistingParent(name + "_magic_power_active", "item/generated")
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/" + name))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/mayurant_magic_layer"));
        
        provider.withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/" + name))
                .override()
                .predicate(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "magic_power"), 1f)
                .model(magicPowerActiveModel)
                .end();
    }
}
