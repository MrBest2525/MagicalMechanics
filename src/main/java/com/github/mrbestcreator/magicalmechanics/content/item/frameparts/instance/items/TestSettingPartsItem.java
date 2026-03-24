package com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.items;

import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SettingParts;
import com.github.mrbestcreator.magicalmechanics.content.item.frameparts.instance.SettingPartsInstance;
import net.minecraft.world.item.Item;

public class TestSettingPartsItem extends Item implements SettingParts {
    public TestSettingPartsItem(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public SettingPartsInstance createInstance() {
        return null;
    }
}
