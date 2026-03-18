package com.github.mrbestcreator.magicalmechanics.datagen.common.recipe;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.IMachineFrameTier;
import com.github.mrbestcreator.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItems;
import com.github.mrbestcreator.magicalmechanics.content.item.mayurant.MayurantTiers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }
    
    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        // BaseFrame
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BASE_FRAME.get())
                .pattern("PSP")
                .pattern("S S")
                .pattern("PSP")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_sticks", inventoryTrigger(
                        ItemPredicate.Builder.item()
                                .of(ItemTags.PLANKS)
                                .of(Items.STICK)
                                .build()
                ))
                .save(output);
        
        // MachineFrames
        for (IMachineFrameTier tier: MachineFrameTiers.values()) {
            var resultBlock = ModBlocks.MACHINE_FRAMES.get(tier.getMachineFrameId()).get();
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, resultBlock)
                    .requires(ModBlocks.BASE_FRAME.get())
                    .requires(tier.getAssemblyItem())
                    .unlockedBy("has_base_frame", inventoryTrigger(
                            ItemPredicate.Builder.item()
                                    .of(ModBlockItems.BASE_FRAME_ITEM.get())
                                    .build()
                    ))
                    .unlockedBy("has_" + tier.getMachineFrameId() + "_material", inventoryTrigger(
                            ItemPredicate.Builder.item()
                                    .of(tier.getAssemblyItem())
                                    .build()
                    ))
                    .save(output);
        }
        
        // Mayurants
        for (MayurantTiers tier : MayurantTiers.values()) {
            Item resultItem = ModItems.MAYURANT_ITEMS.get(tier.getId()).get();
            
            if (tier.getIsSmithingTable()) {
                // 【鍛冶台レシピ】
                // ネザーライト等の特殊なアップグレード用
                SmithingTransformRecipeBuilder.smithing(
                                tier.getCoreItem(),
                                tier.getBaseItem(),
                                tier.getAssemblyItem(),
                                RecipeCategory.TOOLS,
                                resultItem
                        )
                        .unlocks("has_" + tier.getId() + "_material_mirrored", inventoryTrigger(
                                ItemPredicate.Builder.item().of(
                                        Arrays.stream(tier.getAssemblyItem().getItems())
                                                .map(ItemStack::getItem)
                                                .toArray(Item[]::new)
                                )
                        ))
                        .save(output, MagicalMechanics.MODID + ":" + tier.getId() + "_mayurant_smithing");
                
            } else {
                // 【作業台レシピ】
                // 左右反転（Mirrored）は、現在のRecipeProviderではJSONを手動でいじるか、
                // もしくは単純に pattern を入れ替えたものを2つ登録する手法が一般的です。
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, resultItem)
                        .pattern("  P")
                        .pattern(" A ")
                        .pattern("M  ")
                        .define('P', tier.getBaseItem())
                        .define('A', Items.AMETHYST_SHARD)
                        .define('M', tier.getAssemblyItem())
                        .unlockedBy("has_" + tier.getId() + "_material", inventoryTrigger(
                                ItemPredicate.Builder.item().of(
                                        Arrays.stream(tier.getAssemblyItem().getItems())
                                                .map(ItemStack::getItem)
                                                .toArray(Item[]::new)
                                )
                        ))
                        .save(output);
                
                // 左右反転もサポートしたい場合（簡易的な方法）
        
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, resultItem)
                        .pattern("P  ")
                        .pattern(" A ")
                        .pattern("  M")
                        .define('P', tier.getBaseItem())
                        .define('A', Items.AMETHYST_SHARD)
                        .define('M', tier.getAssemblyItem())
                        .unlockedBy("has_" + tier.getId() + "_material", inventoryTrigger(
                                ItemPredicate.Builder.item().of(
                                        Arrays.stream(tier.getAssemblyItem().getItems())
                                                .map(ItemStack::getItem)
                                                .toArray(Item[]::new)
                                )
                        ))
                        .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, tier.getId() + "_mirrored"));
        
            }
        }
    }
}
