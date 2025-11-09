package dev.khanhtimn.gravitychanger.block;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.block.blocks.GravityPlatingBlock;
import dev.khanhtimn.gravitychanger.block.blocks.GravityPlatingBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GravityBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(GravityChanger.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GravityChanger.MODID);

    public static final DeferredHolder<Block, GravityPlatingBlock> GRAVITY_PLATING = BLOCKS.register("plating", () -> new GravityPlatingBlock(BlockBehaviour.Properties.of().noOcclusion().noCollission().instabreak()));

    public static final Supplier<BlockEntityType<GravityPlatingBlockEntity>> GRAVITY_PLATING_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("plating",
                    () -> BlockEntityType.Builder.of(GravityPlatingBlockEntity::new,
                            GravityBlocks.GRAVITY_PLATING.get()).build(null));
}
