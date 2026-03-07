package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum MayurantTires implements Tier {
    WOOD(75, 3.0F, 0.5F, 22, () -> Ingredient.of(ItemTags.PLANKS)),
    STONE(197, 6.0F, 1.5F, 8, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS)),
    IRON(375, 9.0F, 3.0F, 21, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD(48, 18.0F, 0.5F, 33, () -> Ingredient.of(Items.GOLD_NUGGET)),
    DIAMOND(2341, 12.0F, 1.5F, 15, () -> Ingredient.of(Items.DIAMOND)),
    NETHERITE(3046, 13.5F, 6.0F, 22, () -> Ingredient.of(Items.NETHERITE_INGOT));
    
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;
    
    MayurantTires(int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }
    
    @Override public int getUses() { return this.uses; }
    @Override public float getSpeed() { return this.speed; }
    @Override public float getAttackDamageBonus() { return this.attackDamageBonus; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public @NotNull Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    
    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        // ここはバニラのツルハシと同じ判定基準を使い回すのが安全
        return this == NETHERITE ? BlockTags.INCORRECT_FOR_NETHERITE_TOOL :
                this == DIAMOND ? BlockTags.INCORRECT_FOR_DIAMOND_TOOL :
                        BlockTags.INCORRECT_FOR_IRON_TOOL;
    }
}
