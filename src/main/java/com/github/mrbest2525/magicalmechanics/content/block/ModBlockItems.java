package com.github.mrbest2525.magicalmechanics.content.block;

import com.github.mrbest2525.magicalmechanics.MagicalMechanics;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameBlockItem;
import com.github.mrbest2525.magicalmechanics.content.block.machine.frame.MachineFrameTiers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModBlockItems {
    public static final DeferredRegister.Items BLOCK_ITEMS =
            DeferredRegister.createItems(MagicalMechanics.MODID);
    
    public static final DeferredItem<BlockItem> BASE_FRAME_ITEM =
            BLOCK_ITEMS.register("base_frame", () -> new BlockItem(ModBlocks.BASE_FRAME.get(), new Item.Properties()));
    
    public static final DeferredItem<BlockItem> MACHINE_FRAME_BLOCK_ITEM =
            BLOCK_ITEMS.register("machine_frame", () -> new MachineFrameBlockItem(ModBlocks.MACHINE_FRAME.get(), MachineFrameTiers.IRON));
    
    public static final Map<String, DeferredItem<BlockItem>> MACHINE_FRAME_BLOCK_ITEMS = new HashMap<>();
    static {
        for (MachineFrameTiers tier : MachineFrameTiers.values()) {
            String id = tier.getMachineFrameId();
            
            // アイテムの登録
            DeferredItem<BlockItem> registeredItem = BLOCK_ITEMS.register(id, () -> {
                // ここで ModBlocks から対応するブロックを getRenderer() する。
                // 実際に getRenderer() が呼ばれるのはレジストリが動く「後」なので安全。
                Block block = ModBlocks.MACHINE_FRAMES.get(id).get();
                
                return new MachineFrameBlockItem(
                        block,
                        tier
                );
            });
            
            // Mapに入れておく（クリエタブなどで使用）
            MACHINE_FRAME_BLOCK_ITEMS.put(id, registeredItem);
        }
    }
    
    public static final DeferredItem<BlockItem> FE_INPUT_ADAPTER_BLOCK_ITEM =
            BLOCK_ITEMS.register("fe_input_adapter", () -> new BlockItem(ModBlocks.FE_INPUT_ADAPTER_BLOCK.get(), new Item.Properties()));
    
    public static final DeferredItem<BlockItem> FE_OUTPUT_ADAPTER_BLOCK_ITEM =
            BLOCK_ITEMS.register("fe_output_adapter", () -> new BlockItem(ModBlocks.FE_OUTPUT_ADAPTER_BLOCK.get(), new Item.Properties()));
    
    private ModBlockItems() {}
    
    public static void register(IEventBus bus) {
        BLOCK_ITEMS.register(bus);
    }
}
