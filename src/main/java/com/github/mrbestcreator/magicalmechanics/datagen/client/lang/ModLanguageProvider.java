package com.github.mrbestcreator.magicalmechanics.datagen.client.lang;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, MagicalMechanics.MODID, locale);
    }
    
    @Override
    protected void addTranslations() {
    
    }
}
