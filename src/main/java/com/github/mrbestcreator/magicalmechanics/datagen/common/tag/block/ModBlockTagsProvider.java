package com.github.mrbestcreator.magicalmechanics.datagen.common.tag.block;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    
    public static final TagKey<Block> MINEABLE_WITH_MAYURANT =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "mineable/mayurant"));
    
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var myBlock = this.tag(MINEABLE_WITH_MAYURANT).add(ModBlocks.MACHINE_FRAME.get());
        
        // ツルハシかマユラントで掘れるようにする
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.MACHINE_FRAME.get());
        this.tag(MINEABLE_WITH_MAYURANT).add(ModBlocks.MACHINE_FRAME.get());
    }
}
