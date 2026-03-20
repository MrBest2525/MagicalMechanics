package com.github.mrbestcreator.magicalmechanics.datagen.common.loot.block;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class BlockDropLootProvider extends BlockLootSubProvider {
    public BlockDropLootProvider(HolderLookup.Provider lookupProvider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }
    
    @Override
    protected void generate() {
        // TODO マユラントで壊したときにBlockEntityのデータ保持
        // TODO あまりうまくいかないならBlockやBlockEntity側での実装にする(時間かけてキレイよりも後で直すことにして確実に実装できる方を優先する)
        this.dropSelf(ModBlocks.MACHINE_FRAME.get());
        
        this.dropSelf(ModBlocks.BASE_FRAME.get());
        ModBlocks.MACHINE_FRAMES.values().forEach((block) -> this.dropSelf(block.get()));
    }
    
    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        // MOD内の全ブロックをチェック対象にする（ドロップ設定漏れがあるとエラーを出してくれる）
        return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).map(block -> (Block) block)::iterator;
    }
}
