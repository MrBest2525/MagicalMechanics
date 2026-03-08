package com.github.mrbestcreator.magicalmechanics.datagen;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.datagen.client.item.MayurantItemModelProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.client.lang.EnUsLanguageProvider;
import com.github.mrbestcreator.magicalmechanics.datagen.client.lang.JaJpLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MagicalMechanics.MODID)
public class ModDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        // 【クライアント側データ】
        
        // == ITEM ==
        generator.addProvider(
                event.includeClient(),
                new MayurantItemModelProvider(output, existingFileHelper)
        );
        
        // == 翻訳ファイル ==
        generator.addProvider(event.includeClient(), new EnUsLanguageProvider(output));
        generator.addProvider(event.includeClient(), new JaJpLanguageProvider(output));
        
        // 【サーバー側データ】
    }
}
