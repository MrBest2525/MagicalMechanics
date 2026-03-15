package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import com.github.mrbestcreator.magicalmechanics.util.MMLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <h1>MayurantItem</h1>
 * <p>マユラント(Mayurant)アイテム</p>
 */
public class MayurantItem extends DiggerItem {
    
    public static final TagKey<Block> MINEABLE_MAYURANT = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "mineable/mayurant")
    );
    
    private final int maxMagicPower;
    private final int minMagicPower;
    
    /**
     * @param tier MayurantTier
     * @param properties Properties
     */
    public MayurantItem(MayurantTier tier, Properties properties) {
        super(tier, MINEABLE_MAYURANT, properties);
        this.maxMagicPower = tier.getMaxMagicPower();
        this.minMagicPower = tier.getMinMagicPower();
    }
    
    public static @NotNull ItemAttributeModifiers createAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                // 攻撃ダメージ (1.0 + Tierボーナス + ベースダメージ)
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                (attackDamage + tier.getAttackDamageBonus()),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                // 攻撃速度 (基準値4.0に加算されるため、ツルハシなら -2.8 程度を指定)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                attackSpeed,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
    
    /**
     * MagicPowerを取得
     * @param stack ItemStack
     */
    public int getMagicPower(ItemStack stack) {
        return stack.getOrDefault(ModItemDataComponents.MAGIC_POWER.get(), minMagicPower);
    }
    
    /**
     * MagicPowerを置き換える
     * (上限、下限は超えられない)
     * @param stack ItemStack
     * @param magicPower int 置き換えるMagicPower
     */
    public void setMagicPower(ItemStack stack, int magicPower) {
        stack.set(ModItemDataComponents.MAGIC_POWER.get(), Math.max(Math.min(maxMagicPower, magicPower), minMagicPower));
    }
    
    /**
     * MagicPowerに指定量を増やす
     * (上限、下限は超えられない)
     * @param stack ItemStack
     * @param magicPower int 増やすMagicPower
     */
    public void addMagicPower(ItemStack stack, int magicPower) {
        setMagicPower(stack, getMagicPower(stack) + magicPower);
    }
    
    /**
     * MagicPowerから指定量を減らす
     * (上限、下限は超えられない)
     * @param stack ItemStack
     * @param magicPower int 減らすMagicPower
     */
    public void subtractMagicPower(ItemStack stack, int magicPower) {
        setMagicPower(stack, getMagicPower(stack) - magicPower);
    }
    
    public int getMinMagicPower() {
        return minMagicPower;
    }
    
    public int getMaxMagicPower() {
        return maxMagicPower;
    }
    
    /**
     * ツールチップで状態を表示（QOL向上）
     */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        float ratio = Mth.clamp(((float) getMagicPower(stack) - minMagicPower) / (maxMagicPower - minMagicPower), 0.0f, 1.0f);
        if (ratio == 0) {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_EMPTY).withStyle(ChatFormatting.GRAY));
        } else if (ratio <= 0.3){
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FAINT).withStyle(ChatFormatting.DARK_AQUA));
        } else if (ratio <= 0.6) {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_STABLE).withStyle(ChatFormatting.DARK_AQUA));
        } else if (ratio < 1) {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_VIBRANT).withStyle(ChatFormatting.DARK_AQUA));
        } else {
            tooltip.add(Component.translatable(MMLang.Tooltip.Item.MagicalMechanics.Mayurant.MAGIC_POWER_FULL).withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
    
    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        // つるはしの上位互換としての挙動（タグ判定などは後ほど調整可能）
        return super.isCorrectToolForDrops(stack, state);
    }
}
