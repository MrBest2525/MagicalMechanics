package com.github.mrbestcreator.magicalmechanics.content.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> WRENCH =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("magicalmechanics", "wrench"));
}
