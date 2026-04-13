package com.github.mrbest2525.magicalmechanics.datagen.client.item.model.blockitem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredItem;

public class MachineFrameBlockItemModelGenerator {
    private final ItemModelProvider provider;
    
    public MachineFrameBlockItemModelGenerator(ItemModelProvider provider) {
        this.provider = provider;
    }
    
    /**
     * MachineFrameBockItemのモデル生成
     */
    public void generateMachineFrameBlockItem(DeferredItem<BlockItem> blockItem) {
        String id = blockItem.getId().getPath();
        
        // 親を builtin/entity に設定
        ItemModelBuilder builder = provider.getBuilder(id);
        
        
        // 各種トランスフォームの設定
        builder.parent(new ModelFile.UncheckedModelFile(provider.mcLoc("builtin/entity")))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 225, 0)
                .scale(0.625f)
                .end()
                .transform(ItemDisplayContext.GROUND)
                .translation(0, 3, 0)
                .scale(0.25f)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .scale(0.5f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 45, 0)
                .translation(0, 2.5f, 0)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 45, 0)
                .scale(0.4f)
                .end();
    }
}
