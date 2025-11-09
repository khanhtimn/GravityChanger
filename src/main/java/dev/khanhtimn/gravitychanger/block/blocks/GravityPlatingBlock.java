package dev.khanhtimn.gravitychanger.block.blocks;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import dev.khanhtimn.gravitychanger.block.GravityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Based on code from AmethystGravity (by CyborgCabbage)
 */
public class GravityPlatingBlock extends BaseEntityBlock {
    public static final MapCodec<GravityPlatingBlock> CODEC = simpleCodec(GravityPlatingBlock::new);
    // in a corner, multiple faces of plates can occupy the same block

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    protected static final VoxelShape DOWN_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape UP_SHAPE = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape NORTH_SHAPE = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    protected static final VoxelShape WEST_SHAPE = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesByState;

    public GravityPlatingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
        );
        this.shapesByState =
                ImmutableMap.copyOf(
                        this.stateDefinition.getPossibleStates().stream()
                                .collect(Collectors.toMap(Function.identity(), GravityPlatingBlock::getShapeForState))
                );
    }

    public GravityPlatingBlock() {
        this(BlockBehaviour.Properties.of().noOcclusion().noCollission().instabreak());
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelShape = UP_SHAPE;
        }
        if (state.getValue(NORTH)) {
            voxelShape = Shapes.or(voxelShape, SOUTH_SHAPE);
        }
        if (state.getValue(SOUTH)) {
            voxelShape = Shapes.or(voxelShape, NORTH_SHAPE);
        }
        if (state.getValue(EAST)) {
            voxelShape = Shapes.or(voxelShape, WEST_SHAPE);
        }
        if (state.getValue(WEST)) {
            voxelShape = Shapes.or(voxelShape, EAST_SHAPE);
        }
        if (state.getValue(DOWN)) {
            voxelShape = Shapes.or(voxelShape, DOWN_SHAPE);
        }
        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    public static BooleanProperty directionToProperty(Direction direction) {
        return switch (direction) {
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    // Note: the direction is gravity field direction. the facing is the opposite
    public static boolean hasDir(BlockState blockState, Direction dir) {
        return blockState.getValue(directionToProperty(dir));
    }

    public static ArrayList<Direction> getDirections(BlockState blockState) {
        ArrayList<Direction> list = new ArrayList<>();
        //Iterate directions
        for (int directionId = 0; directionId < 6; directionId++) {
            //Convert ID to Direction
            Direction direction = Direction.from3DDataValue(directionId);
            //If the plate has this direction
            if (hasDir(blockState, direction)) {
                list.add(direction);
            }
        }
        return list;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shapesByState.get(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (hasDir(state, direction) && !canPlaceOn(world, pos.relative(direction), direction.getOpposite())) {
            state = state.setValue(directionToProperty(direction), false);
            if (getDirections(state).isEmpty()) {
                return Blocks.AIR.defaultBlockState();
            } else {
                return state;
            }
        } else {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180 -> {
                return (((state.setValue(NORTH, state.getValue(SOUTH))).setValue(EAST, state.getValue(WEST))).setValue(SOUTH, state.getValue(NORTH))).setValue(WEST, state.getValue(EAST));
            }
            case COUNTERCLOCKWISE_90 -> {
                return (((state.setValue(NORTH, state.getValue(EAST))).setValue(EAST, state.getValue(SOUTH))).setValue(SOUTH, state.getValue(WEST))).setValue(WEST, state.getValue(NORTH));
            }
            case CLOCKWISE_90 -> {
                return (((state.setValue(NORTH, state.getValue(WEST))).setValue(EAST, state.getValue(NORTH))).setValue(SOUTH, state.getValue(EAST))).setValue(WEST, state.getValue(SOUTH));
            }
        }
        return state;
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT -> {
                return (state.setValue(NORTH, state.getValue(SOUTH))).setValue(SOUTH, state.getValue(NORTH));
            }
            case FRONT_BACK -> {
                return (state.setValue(EAST, state.getValue(WEST))).setValue(WEST, state.getValue(EAST));
            }
        }
        return super.mirror(state, mirror);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GravityPlatingBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (world.isClientSide)
            return createTickerHelper(type, GravityBlocks.GRAVITY_PLATING_BLOCK_ENTITY.get(), GravityPlatingBlockEntity::tick);
        else
            return createTickerHelper(type, GravityBlocks.GRAVITY_PLATING_BLOCK_ENTITY.get(), GravityPlatingBlockEntity::tick);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        if (!context.isSecondaryUseActive() && context.getItemInHand().getItem() == this.asItem()) {
            return !hasDir(state, context.getClickedFace().getOpposite());
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(directionToProperty(ctx.getClickedFace().getOpposite()), true);
        }
        return defaultBlockState().setValue(directionToProperty(ctx.getClickedFace().getOpposite()), true);
    }

    private boolean canPlaceOn(BlockGetter world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isFaceSturdy(world, pos, side);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        ArrayList<Direction> directions = getDirections(state);
        if (directions.size() == 1) {
            return canPlaceOn(world, pos.relative(directions.getFirst()), directions.getFirst().getOpposite());
        }
        //Placing inside an existing plating
        if (directions.size() > 1) {
            for (Direction dir : getDirections(world.getBlockState(pos))) {
                directions.remove(dir);
            }
            return canPlaceOn(world, pos.relative(directions.getFirst()), directions.getFirst().getOpposite());
        }
        return false;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit
    ) {
        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        Direction hitDir = hit.getDirection();
        Direction plateDir = hitDir.getOpposite();

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof GravityPlatingBlockEntity be)) {
            return ItemInteractionResult.FAIL;
        }

        InteractionResult result = be.interact(level, pos, plateDir, player, hand);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    /**
     * Similar to {@link ShulkerBoxBlock#playerWillDestroy(Level, BlockPos, BlockState, Player)}
     * Make it drop in creative mode.
     */
    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof GravityPlatingBlockEntity be &&
                !level.isClientSide && player.isCreative()
        ) {
            List<ItemStack> drops = be.getDrops();

            for (ItemStack itemStack : drops) {
                ItemEntity itemEntity = new ItemEntity(
                        level,
                        (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5,
                        itemStack
                );
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof GravityPlatingBlockEntity be) {
            return be.getDrops();
        }

        return List.of();
    }
}
