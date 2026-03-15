package com.github.mrbestcreator.magicalmechanics.content.util;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ModTags {
    public static class Items {
        public static final Map<String, TagKey<Item>> ALL_TAGS = new HashMap<>();
        // Item Tags
        public static final TagKey<Item> WRENCH_ITEMS = createTag("wrench");
        public static final TagKey<Item> MAYURANT_ITEMS = createTag("mayurant");
        public static final TagKey<Item> FRAME_CORE_PARTS = createTag("frame_core_parts");
        public static final TagKey<Item> FRAME_SIDE_PARTS = createTag("frame_side_parts");
        
        private static TagKey<Item> createTag(String name) {
            TagKey<Item> newTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, name));
            ALL_TAGS.put(name, newTag);
            return newTag;
        }
    }
    
    public static class Blocks {
        public static final Map<String, TagKey<Block>> ALL_TAGS = new HashMap<>();
        
        // Block Tags
        
        private static TagKey<Block> createTag(String name) {
            TagKey<Block> newTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, name));
            ALL_TAGS.put(name, newTag);
            return newTag;
        }
    }
}
