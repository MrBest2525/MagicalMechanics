package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum MayurantTiers implements MayurantTier {
    WOOD(75, 3.0F, 0.5F, 22, () -> Ingredient.of(ItemTags.PLANKS), 0, 100_000),
    STONE(197, 6.0F, 1.5F, 8, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS), 0, 500_000),
    IRON(375, 9.0F, 3.0F, 21, () -> Ingredient.of(Items.IRON_INGOT), 0, 1_000_000),
    GOLD(48, 18.0F, 0.5F, 33, () -> Ingredient.of(Items.GOLD_NUGGET), 0, 50_000_000),
    DIAMOND(2341, 12.0F, 4.5F, 15, () -> Ingredient.of(Items.DIAMOND), 0, 5_000_000),
    NETHERITE(3046, 13.5F, 6.0F, 22, () -> Ingredient.of(Items.NETHERITE_INGOT), 0, 10_000_000);
    
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;
    private final int minMagicPower;
    private final int maxMagicPower;
    
    MayurantTiers(int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient, int minMagicPowet, int maxMagicPower) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
        this.minMagicPower = minMagicPowet;
        this.maxMagicPower = maxMagicPower;
    }
    
    @Override public int getUses() { return this.uses; }
    @Override public float getSpeed() { return this.speed; }
    @Override public float getAttackDamageBonus() { return this.attackDamageBonus; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public @NotNull Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    @Override public int getMinMagicPower() {return minMagicPower;}
    @Override public int getMaxMagicPower() {return maxMagicPower;}
    
    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        // ここはバニラのツルハシと同じ判定基準を使い回すのが安全
        return this == NETHERITE ? BlockTags.INCORRECT_FOR_NETHERITE_TOOL :
                this == DIAMOND ? BlockTags.INCORRECT_FOR_DIAMOND_TOOL :
                        BlockTags.INCORRECT_FOR_IRON_TOOL;
    }
    
}
