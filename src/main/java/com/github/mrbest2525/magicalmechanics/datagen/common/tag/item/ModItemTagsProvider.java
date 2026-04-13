package com.github.mrbest2525.magicalmechanics.datagen.common.tag.item;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.content.util.ModTags;
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
    
    public static final TagKey<Item> ENERGY_MACHINE_PART_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "energy_machine_part"));
    
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lockupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lockupProvider, blockTags, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var wrenchTag = this.tag(ModTags.Items.WRENCH_ITEMS);
        ModItems.WRENCH_ITEMS.values().forEach((deferredItem -> wrenchTag.add(deferredItem.get())));
        
        var mayurantTag = this.tag(ModTags.Items.MAYURANT_ITEMS);
        ModItems.MAYURANT_ITEMS.values().forEach((deferredItem) -> mayurantTag.add(deferredItem.get()));
        this.tag(ItemTags.PICKAXES).addTag(ModTags.Items.MAYURANT_ITEMS);
        
        var machineCorePartsTag = this.tag(ModTags.Items.FRAME_CORE_PARTS);
        ModItems.MACHINE_CORE_PARTS.values().forEach((deferredItem -> machineCorePartsTag.add(deferredItem.get())));
        
        var machineSidePartsTag = this.tag(ModTags.Items.FRAME_SIDE_PARTS);
        ModItems.MACHINE_SIDE_PARTS.values().forEach((deferredItem -> machineSidePartsTag.add(deferredItem.get())));
        
        // EnergyMachinePart
        this.tag(ENERGY_MACHINE_PART_TAG).add(ModItems.NORMAL_ENERGY_CORE.get());
        this.tag(ENERGY_MACHINE_PART_TAG).add(ModItems.ADVANCED_ENERGY_CORE.get());
        this.tag(ENERGY_MACHINE_PART_TAG).add(ModItems.UNLIMITED_ENERGY_CORE.get());
        this.tag(ENERGY_MACHINE_PART_TAG).add(ModItems.UNLIMITED_WIRELESS_ENERGY_INTERFACE.get());
    }
}
