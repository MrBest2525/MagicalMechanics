package com.github.mrbestcreator.magicalmechanics.datagen.common.loot;

import com.github.mrbestcreator.magicalmechanics.datagen.common.loot.block.BlockDropLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(
                        // ここで「君が作ったクラス」を登録する！
                        new LootTableProvider.SubProviderEntry(BlockDropLootProvider::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider
        );
    }
}
