package io.github.mrbest2525.magicalmechanics.datagen.common.tag.block;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.IMachineFrameTier;
import io.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
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
        
        
        var pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        
        for (DeferredBlock<Block> block : ModBlocks.MACHINE_FRAMES.values()) {
            pickaxeTag.add(block.get());
            
            if (block.get() instanceof MachineFrameBlock machineFrameBlock) {
                IMachineFrameTier tier = machineFrameBlock.getMachineFrameTier();
                if (tier.getToolLevelTag() != null) {
                    tag(tier.getToolLevelTag()).add(block.get());
                }
            }
        }
        
        // マユラントはつるはしを継承する
        this.tag(MINEABLE_WITH_MAYURANT).addTag(BlockTags.MINEABLE_WITH_PICKAXE);
        
        // つるはしで掘れるものに追加する
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.FE_INPUT_ADAPTER_BLOCK.get())
                .add(ModBlocks.FE_OUTPUT_ADAPTER_BLOCK.get());
    }
}
