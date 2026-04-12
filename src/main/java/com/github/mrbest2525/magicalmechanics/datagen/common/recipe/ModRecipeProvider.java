package com.github.mrbest2525.magicalmechanics.datagen.common.recipe;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlockItems;
import com.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.IMachineFrameTier;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import com.github.mrbest2525.magicalmechanics.content.item.ModItems;
import com.github.mrbest2525.magicalmechanics.content.item.mayurant.MayurantTiers;
import com.github.mrbest2525.magicalmechanics.datagen.common.tag.item.ModItemTagsProvider;
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
        
        // Wrench
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WRENCH)
                .pattern(" N ")
                .pattern(" IN")
                .pattern("I  ")
                .define('I', Items.IRON_INGOT)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_" + Items.IRON_INGOT + "_material", has(Items.IRON_INGOT))
                .save(output);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WRENCH)
                .pattern(" N ")
                .pattern("NI ")
                .pattern("  I")
                .define('I', Items.IRON_INGOT)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_" + Items.IRON_INGOT + "_material", has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.WRENCH.getId().getPath() + "_mirrored"));
        
        // mf_link_staff
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MF_LINK_STAFF)
                .pattern(" IA")
                .pattern(" SI")
                .pattern("S  ")
                .define('A', Items.AMETHYST_SHARD)
                .define('I', Items.IRON_NUGGET)
                .define('S', Items.STICK)
                .unlockedBy("has_energy_part", has(ModItemTagsProvider.ENERGY_MACHINE_PART_TAG))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.MF_LINK_STAFF.getId().getPath()));
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MF_LINK_STAFF)
                .pattern("AI ")
                .pattern("IS ")
                .pattern("  S")
                .define('A', Items.AMETHYST_SHARD)
                .define('I', Items.IRON_NUGGET)
                .define('S', Items.STICK)
                .unlockedBy("has_energy_part", has(ModItemTagsProvider.ENERGY_MACHINE_PART_TAG))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.MF_LINK_STAFF.getId().getPath() + "_mirrored"));
        
        // fe_input_adapter_block
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM)
                .pattern("GAG")
                .pattern("A A")
                .pattern("GAG")
                .define('A', Items.AMETHYST_SHARD)
                .define('G', Items.GLASS_PANE)
                .unlockedBy("has_energy_part", has(ModItemTagsProvider.ENERGY_MACHINE_PART_TAG))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM.getId().getPath()));
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM)
                .requires(ModBlockItems.FE_OUTPUT_ADAPTER_BLOCK_ITEM)
                .unlockedBy("has_energy_part", has(ModItemTagsProvider.ENERGY_MACHINE_PART_TAG))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM.getId().getPath() + "_from_output"));
        
        // fe_output_adapter_block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlockItems.FE_OUTPUT_ADAPTER_BLOCK_ITEM)
                .requires(ModBlockItems.FE_INPUT_ADAPTER_BLOCK_ITEM)
                .unlockedBy("has_energy_core", has(ModItemTagsProvider.ENERGY_MACHINE_PART_TAG))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModBlockItems.FE_OUTPUT_ADAPTER_BLOCK_ITEM.getId().getPath() + "_from_input"));
        
        // normal_energy_core
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.NORMAL_ENERGY_CORE)
                .pattern(" I ")
                .pattern("IAI")
                .pattern(" I ")
                .define('A', Items.AMETHYST_SHARD)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_" + Items.IRON_INGOT, has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.NORMAL_ENERGY_CORE.getId().getPath()));
        
        // advanced_energy_core
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.ADVANCED_ENERGY_CORE)
                .pattern(" I ")
                .pattern("IAI")
                .pattern(" I ")
                .define('A', Items.AMETHYST_BLOCK)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_" + Items.IRON_INGOT, has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.ADVANCED_ENERGY_CORE.getId().getPath()));
        
        // unlimited_energy_core
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.UNLIMITED_ENERGY_CORE)
                .pattern(" I ")
                .pattern("IAI")
                .pattern(" I ")
                .define('A', Items.AMETHYST_BLOCK)
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("has_" + Items.IRON_BLOCK, has(Items.IRON_BLOCK))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.UNLIMITED_ENERGY_CORE.getId().getPath()));
        
        // unlimited_wireless_energy_interface
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.UNLIMITED_WIRELESS_ENERGY_INTERFACE)
                .pattern("HHA")
                .pattern("BDB")
                .pattern("S S")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STONE_BUTTON)
                .define('D', Items.DIAMOND)
                .define('H', Items.STONE_SLAB)
                .define('S', Items.STONE_STAIRS)
                .unlockedBy("has_" + Items.DANDELION, has(Items.DIAMOND))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.UNLIMITED_WIRELESS_ENERGY_INTERFACE.getId().getPath()));
        
        // furnace_core
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.FURNACE_CORE)
                .pattern("SSS")
                .pattern("SFS")
                .pattern("SSS")
                .define('F', Items.FURNACE)
                .define('S', Items.STONE)
                .unlockedBy("has_" + Items.FURNACE, has(Items.FURNACE))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.FURNACE_CORE.getId().getPath()));
        
        // furnace_side
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.FURNACE_SIDE)
                .pattern("SSS")
                .pattern("SFS")
                .pattern("SSS")
                .define('F', Items.FURNACE)
                .define('S', Items.STONE_SLAB)
                .unlockedBy("has_" + Items.FURNACE, has(Items.FURNACE))
                .save(output, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, ModItems.FURNACE_SIDE.getId().getPath()));
    }
}
