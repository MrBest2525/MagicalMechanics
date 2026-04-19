package io.github.mrbest2525.magicalmechanics.datagen.client.block;

import io.github.mrbest2525.magicalmechanics.MagicalMechanics;
import io.github.mrbest2525.magicalmechanics.content.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MachineFramesBlockModelProvider extends BlockStateProvider {
    public MachineFramesBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MagicalMechanics.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerStatesAndModels() {
        for (DeferredBlock<Block> block: ModBlocks.MACHINE_FRAMES.values()) {
            generateMachineFrameModel(block);
        }
    }
    
    /**
     * MachineFrame用のModel.json生成
     */
    private void generateMachineFrameModel(DeferredBlock<Block> block) {
        String id = block.getId().getPath();
        
        var model = models().withExistingParent(id, modLoc("block/machine_frame"))
                .texture("all", modLoc("block/machine_frame/" + id)) // 親モデルの変数名に合わせて調整
                .texture("particle", modLoc("block/machine_frame/" + id));
        
        // 2. BlockStateの生成（作成したモデルをブロックに紐付け）
        simpleBlock(block.get(), model);
        
    }
    
}
