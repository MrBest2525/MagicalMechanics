package com.github.mrbestcreator.magicalmechanics.datagen.common.tag.item;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    
    public static final TagKey<Item> MAYURANTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "tools/mayurants"));
    
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lockupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lockupProvider, blockTags, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var mayurantTag = this.tag(MAYURANTS);
        ModItems.MAYURANT_ITEMS.forEach((deferredItem) -> mayurantTag.add(deferredItem.get()));
    }
}
