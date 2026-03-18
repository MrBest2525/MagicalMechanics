package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum MayurantTiers implements MayurantTier {
    WOOD("wooden_mayurant", 75, 3.0F, 0.5F, 22, Tiers.WOOD::getRepairIngredient, 0, 100_000, false, Ingredient.of(Items.WOODEN_PICKAXE), Tiers.WOOD.getRepairIngredient(), Ingredient.of(Items.AMETHYST_SHARD)),
    STONE("stone_mayurant", 197, 6.0F, 1.5F, 8, Tiers.STONE::getRepairIngredient, 0, 500_000, false, Ingredient.of(Items.STONE_PICKAXE), Tiers.STONE.getRepairIngredient(), Ingredient.of(Items.AMETHYST_SHARD)),
    IRON("iron_mayurant", 375, 9.0F, 3.0F, 21, Tiers.IRON::getRepairIngredient, 0, 1_000_000, false, Ingredient.of(Items.IRON_PICKAXE), Tiers.IRON.getRepairIngredient(), Ingredient.of(Items.AMETHYST_SHARD)),
    GOLD("golden_mayurant", 48, 18.0F, 0.5F, 33, Tiers.GOLD::getRepairIngredient, 0, 50_000_000, false, Ingredient.of(Items.GOLDEN_PICKAXE), Tiers.GOLD.getRepairIngredient(), Ingredient.of(Items.AMETHYST_SHARD)),
    DIAMOND("diamond_mayurant", 2341, 12.0F, 4.5F, 15, Tiers.DIAMOND::getRepairIngredient, 0, 5_000_000, false, Ingredient.of(Items.DIAMOND_PICKAXE), Tiers.DIAMOND.getRepairIngredient(), Ingredient.of(Items.AMETHYST_SHARD)),
    NETHERITE("netherite_mayurant", 3046, 13.5F, 6.0F, 22, Tiers.NETHERITE::getRepairIngredient, 0, 10_000_000, true, Ingredient.of(Items.NETHERITE_PICKAXE), Tiers.NETHERITE.getRepairIngredient(), Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
    // TODO コアとなるアイテムをそのうち実装して置き換える
    
    
    private final String id;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;
    private final int minMagicPower;
    private final int maxMagicPower;
    private final boolean isSmithingTable;
    private final Ingredient baseItem;
    private final Ingredient assemblyItem;
    private final Ingredient coreItem;
    
    MayurantTiers(String id, int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient, int minMagicPower, int maxMagicPower, boolean isSmithingTable, Ingredient baseItem, Ingredient assemblyItem, Ingredient coreItem) {
        this.id = id;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
        this.minMagicPower = minMagicPower;
        this.maxMagicPower = maxMagicPower;
        this.isSmithingTable = isSmithingTable;
        this.baseItem = baseItem;
        this.assemblyItem = assemblyItem;
        this.coreItem = coreItem;
    }
    
    @Override public String getId() {return this.id;}
    @Override public int getUses() { return this.uses; }
    @Override public float getSpeed() { return this.speed; }
    @Override public float getAttackDamageBonus() { return this.attackDamageBonus; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public @NotNull Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    @Override public int getMinMagicPower() {return this.minMagicPower;}
    @Override public int getMaxMagicPower() {return this.maxMagicPower;}
    @Override public boolean getIsSmithingTable() {return this.isSmithingTable;}
    @Override public Ingredient getBaseItem() {return this.baseItem;}
    @Override public Ingredient getAssemblyItem() {return this.assemblyItem;}
    @Override public Ingredient getCoreItem() {return this.coreItem;}
    
    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        // ここはバニラのツルハシと同じ判定基準を使い回すのが安全
        return this == NETHERITE ? BlockTags.INCORRECT_FOR_NETHERITE_TOOL :
                this == DIAMOND ? BlockTags.INCORRECT_FOR_DIAMOND_TOOL :
                        BlockTags.INCORRECT_FOR_IRON_TOOL;
    }
    
}
