package com.github.mrbestcreator.magicalmechanics.content.block.machine.frame;

import com.github.mrbestcreator.magicalmechanics.content.block.ModBlockEntities;
import com.github.mrbestcreator.magicalmechanics.content.menu.block.machine.frame.FrameBlockMenu;
import com.github.mrbestcreator.magicalmechanics.content.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MachineFrameBlock extends TransparentBlock implements EntityBlock {
    
    private final IMachineFrameTier machineFrameTier;
    
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public MachineFrameBlock(IMachineFrameTier tier) {
        // TODO Tireも受け取るように変更すること！！！
        super(tier.getBlockProperties());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        
        this.machineFrameTier = tier;
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new MachineFrameBlockEntity(blockPos, blockState);
    }
    
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> actualType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == actualType ? (BlockEntityTicker<A>) ticker : null;
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
//        return blockEntityType == ModBlockEntities.MACHINE_FRAME.get()
//                ? (lvl, pos, st, be) -> ((MachineFrameBlockEntity) be).tick(lvl, pos, st)
//                : null;
        return createTickerHelper(blockEntityType, ModBlockEntities.MACHINE_FRAME.get(),
                (lvl, pos, st, be) -> be.tick(lvl, pos, st));
    }
    
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        
        if (itemStack.is(ModTags.Items.WRENCH_ITEMS)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof MachineFrameBlockEntity FBe) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, player1) -> new FrameBlockMenu(id, inv, FBe),
                    Component.literal("Frame")
            ), blockPos); // ここでGUIが開く
        }
        
        return ItemInteractionResult.CONSUME;
    }
    
    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        
        // 1. 破壊に使ったツールと、BEを取得
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        
        if (be instanceof MachineFrameBlockEntity frameBe) {
            // 2. マユラントで掘った場合：ドロップするアイテムにBEの全データを書き込む
            if (tool.is(ModTags.Items.MAYURANT_ITEMS)) {
                for (ItemStack drop : drops) {
                    if (drop.is(this.asItem())) {
                        // BEの全データをNBTとして取得
                        CompoundTag tag = new CompoundTag();
                        frameBe.saveAdditional(tag, builder.getLevel().registryAccess());
                        tag.putString("id", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType()).toString());
                        
                        // アイテムの block_entity_data コンポーネントに流し込む
                        drop.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
                    }
                }
            }
            // 3. マユラント以外で掘った場合：地面にパーツをぶちまける
            else {
                frameBe.onRemove();
                for (ItemStack part : frameBe.getParts()) {
                    if (!part.isEmpty()) {
                        // getDropsの中で直接ドロップリストに追加
                        drops.add(part.copy());
                    }
                }
                // BlockEntity側の中身を空にする（onRemoveでの二重ドロップ防止）
                frameBe.clearContent();
            }
        }
        return drops;
    }
    
    @Override
    protected boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side) {
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // プレイヤーの向いている方向の逆にセット（正面を向かせる）
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    public IMachineFrameTier getMachineFrameTier() {
        return machineFrameTier;
    }
}
