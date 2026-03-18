package com.github.mrbestcreator.magicalmechanics.datagen;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.datagen.client.block.MachineFramesBlockModelProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.client.item.ModItemModelProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.client.lang.EnUsLanguageProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.client.lang.JaJpLanguageProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.common.loot.ModLootTableProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.common.recipe.ModRecipeProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.common.tag.block.ModBlockTagsProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.common.tag.item.ModItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MagicalMechanics.MODID)
public class ModDataGenerators {
    @SubscribeEvent
    public static <ModBlockTagProvider> void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        // 【クライアント側データ】
        
        // == BLOCK ==
        generator.addProvider(
                event.includeClient(),
                new MachineFramesBlockModelProvider(output, existingFileHelper)
        );
        // == ITEM ==
        generator.addProvider(
                event.includeClient(),
                new ModItemModelProvider(output, existingFileHelper)
        );
        
        
        // == 翻訳ファイル ==
        generator.addProvider(event.includeClient(), new EnUsLanguageProvider(output));
        generator.addProvider(event.includeClient(), new JaJpLanguageProvider(output));
        
        // 【サーバー側データ】
        
        // == BlockTags ==
        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        
        // == ItemTags ==
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        
        // == BlockDropLoot ==
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output, lookupProvider));
        
        // == Recipe ==
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));
    }
}
