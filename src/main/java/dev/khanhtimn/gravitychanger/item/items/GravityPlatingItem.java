package dev.khanhtimn.gravitychanger.item.items;

import dev.khanhtimn.gravitychanger.block.blocks.GravityPlatingBlockEntity;
import dev.khanhtimn.gravitychanger.data.GravityComponents;
import dev.khanhtimn.gravitychanger.data.components.PlatingComponent;
import dev.khanhtimn.gravitychanger.item.GravityItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GravityPlatingItem extends BlockItem {

    public GravityPlatingItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static @Nullable PlatingComponent getPlatingData(ItemStack stack) {
        return stack.get(GravityComponents.PLATING_SIDE_DATA);
    }

    public static void setPlatingData(ItemStack stack, @Nullable PlatingComponent data) {
        if (data != null) {
            stack.set(GravityComponents.PLATING_SIDE_DATA, data);
        } else {
            stack.remove(GravityComponents.PLATING_SIDE_DATA);
        }
    }

    public static ItemStack createStack(@Nullable PlatingComponent data) {
        ItemStack itemStack = new ItemStack(GravityItems.GRAVITY_PLATING.get());
        setPlatingData(itemStack, data);
        return itemStack;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        PlatingComponent data = getPlatingData(stack);
        if (data != null) {
            return Component.translatable(
                    "gravity_changer.plating.item_name",
                    data.level(), GravityPlatingBlockEntity.getForceText(data.isAttracting())
            );
        }

        return super.getName(stack);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        var level = context.getLevel();
        ItemStack itemStack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();

        if (level.isClientSide()) {
            return result;
        }

        PlatingComponent data = getPlatingData(itemStack);

        if (data != null) {
            BlockEntity blockEntity = level.getBlockEntity(clickedPos);
            if (blockEntity instanceof GravityPlatingBlockEntity be) {
                GravityPlatingBlockEntity.SideData sideData = new GravityPlatingBlockEntity.SideData(
                        data.isAttracting(), data.level()
                );
                be.onPlacing(context.getClickedFace().getOpposite(), sideData);
            }
        }

        return result;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipComponents, flag);
        tooltipComponents.add(Component.translatable("gravity_changer.plating.tooltip.0").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("gravity_changer.plating.tooltip.1").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("gravity_changer.plating.tooltip.2").withStyle(ChatFormatting.GRAY));
    }
}