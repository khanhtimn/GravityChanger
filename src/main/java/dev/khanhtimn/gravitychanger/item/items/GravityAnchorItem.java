package dev.khanhtimn.gravitychanger.item.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// based on AmethystGravity
public class GravityAnchorItem extends Item {
    public final Direction direction;

    public GravityAnchorItem(Properties settings, Direction _direction) {
        super(settings);
        direction = _direction;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(
                Component.translatable("gravity_changer.gravity_anchor.tooltip.0")
                        .withStyle(ChatFormatting.GRAY)
        );

        tooltipComponents.add(
                Component.translatable("gravity_changer.gravity_anchor.tooltip.1")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}
