package com.github.mrbestcreator.magicalmechanics.content.util;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public class Items {
        public static final TagKey<Item> WRENCH_ITEMS = createTag("wrench");
        
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, name));
        }
    }
}
