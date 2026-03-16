package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

public enum MachineFrameTiers implements IMachineFrameTier{
    // TODO Iron以外もちゃんと設定すること
    WOOD("wood_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(2.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion(),
            new Item.Properties(),
            ItemTags.LOGS,
            new MachineFrameStat(false)),
    STONE("stone_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion(),
            new Item.Properties(),
            ItemTags.STONE_TOOL_MATERIALS,
            new MachineFrameStat(false)),
    IRON("iron_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(2.5f, 5)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .isViewBlocking((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .lightLevel((state) -> 1),
            new Item.Properties(),
            Tags.Items.INGOTS_IRON,
            new MachineFrameStat(false)),
    GOLD("gold_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion(),
            new Item.Properties(),
            Tags.Items.INGOTS_GOLD,
            new MachineFrameStat(false)),
    DIAMOND("diamond_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(5.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL).
                    noOcclusion(),
            new Item.Properties(),
            Tags.Items.GEMS_DIAMOND,
            new MachineFrameStat(false)),
    NETHERITE("netherite_machine_frame",
            BlockBehaviour.Properties.of()
                    .strength(5.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK).explosionResistance(1200f)
                    .noOcclusion(),
            new Item.Properties(),
            Tags.Items.INGOTS_NETHERITE,
            new MachineFrameStat(true));
    
    private final String machineFrameId;
    private final BlockBehaviour.Properties blockProperties;
    private final Item.Properties itemProperties;
    private final TagKey<Item> assemblyItem;
    private final MachineFrameStat machineFrameStat;
    
    MachineFrameTiers(String id, BlockBehaviour.Properties blockProperties, Item.Properties itemProperties, TagKey<Item> assemblyItem, MachineFrameStat machineFrameStat) {
        this.machineFrameId = id;
        this.blockProperties = blockProperties;
        this.itemProperties = itemProperties;
        this.assemblyItem = assemblyItem;
        this.machineFrameStat = machineFrameStat;
    }
    
    @Override
    public String getMachineFrameId() {
        return machineFrameId;
    }
    
    @Override
    public BlockBehaviour.Properties getBlockProperties() {
        return blockProperties;
    }
    
    @Override
    public Item.Properties getItemProperties() {
        return itemProperties;
    }
    
    
    @Override
    public TagKey<Item> getAssemblyItem() {
        return assemblyItem;
    }
    
    @Override
    public MachineFrameStat getMachineFrameStat() {
        return machineFrameStat;
    }
}
