package com.github.mrbestcreator.magicalmechanics.content.item.mayurant;

import com.github.mrbestcreator.magicalmechanics.MagicalMechanics;
import com.github.mrbestcreator.magicalmechanics.content.item.ModItemDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <h1>MayurantItem</h1>
 * <p>マユラント(Mayurant)アイテム</p>
 */
public class MayurantItem extends DiggerItem {
    private final int maxMagicPower;
    private final int minMagicPower;
    
    /**
     * @param tier Tire
     * @param properties Properties
     * @param maxMagicPower int<p>MagicPowerの最大値を設定</p>
     * @param minMagicPower int<p>MagicPowerの最小値を設定</p>
     */
    public MayurantItem(Tier tier, Properties properties, int maxMagicPower, int minMagicPower) {
        super(tier, TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MagicalMechanics.MODID, "mineable/mayurant")), properties);
        this.maxMagicPower = maxMagicPower;
        this.minMagicPower = minMagicPower;
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
            tooltip.add(Component.translatable("tooltip.item.magicalmechanics.mayurant.magic_power_empty").withStyle(ChatFormatting.GRAY));
        } else if (ratio <= 0.3){
            tooltip.add(Component.translatable("tooltip.item.magicalmechanics.mayurant.magic_power_faint").withStyle(ChatFormatting.DARK_AQUA));
        } else if (ratio <= 0.6) {
            tooltip.add(Component.translatable("tooltip.item.magicalmechanics.mayurant.magic_power_stable").withStyle(ChatFormatting.DARK_AQUA));
        } else if (ratio < 1) {
            tooltip.add(Component.translatable("tooltip.item.magicalmechanics.mayurant.magic_power_vibrant").withStyle(ChatFormatting.DARK_AQUA));
        } else {
            tooltip.add(Component.translatable("tooltip.item.magicalmechanics.mayurant.magic_power_overflowing").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
    
    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        // つるはしの上位互換としての挙動（タグ判定などは後ほど調整可能）
        return super.isCorrectToolForDrops(stack, state);
    }
    
    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity miner) {
        // サーバー側かつ、サバイバルモードのプレイヤーが壊した時のみ処理
        if (!level.isClientSide && miner instanceof Player player && !player.getAbilities().instabuild) {
            
            // 1. ブロックの硬さ（採掘速度の基準値）を取得
            float destroySpeed = state.getDestroySpeed(level, pos);
            
            // 2. 消費する魔力量を計算 (例: 硬さ 1.5 の石なら 15 消費、硬さ 50 の黒曜石なら 500 消費など)
            // 係数（10.0fなど）はバランスを見て調整してください
            int cost = Math.max(1, (int) (destroySpeed * 10.0f));
            // 計算を合わせる
            cost *= 1000;
            
            // 3. 現在の魔力を取得して減らす
            int currentPower = getMagicPower(stack);
            if (currentPower > minMagicPower) {
                // 魔力が足りない場合は minMagicPower に、足りる場合は計算分を引く
                subtractMagicPower(stack, cost);
                
                // 魔力を使い果たした時のフィードバック（音など）を入れると親切
                if (getMagicPower(stack) == minMagicPower) {
                    level.playSound(null, miner.getX(), miner.getY(), miner.getZ(),
                            SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 0.5f, 1.2f);
                }
            }
        }
        
        return super.mineBlock(stack, level, state, pos, miner);
    }
}
