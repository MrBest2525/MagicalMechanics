package com.github.mrbestcreator.magicalmechanics.datagen.client.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class MayurantItemModelProvider extends ItemModelProvider {
    public MayurantItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        for (DeferredItem<Item> mayurantItem: ModItems.MAYURANT_ITEMS.values()) {
            generateMayurantModel(mayurantItem);
        }
    }
    
    /**
     * マユラント専用のモデル生成
     */
    private void generateMayurantModel(DeferredItem<?> item) {
        // 登録名（例: "iron_mayurant"）を取得
        String name = item.getId().getPath();
        
        ItemModelBuilder magicPowerActiveModel = withExistingParent(name + "_magic_power_active", "item/generated")
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/" + name))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/mayurant_magic_layer"));
        
        withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "item/tool/mayurant/" + name))
                .override()
                    .predicate(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "magic_power"), 1f)
                .model(magicPowerActiveModel)
                .end();
        
        
    }
}
