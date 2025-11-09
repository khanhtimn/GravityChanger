package dev.khanhtimn.gravitychanger.item.items;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GravityChangerItem extends Item {
    public final Direction gravityDirection;

    public GravityChangerItem(Properties settings, Direction _gravityDirection) {
        super(settings);
        gravityDirection = _gravityDirection;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        if (!world.isClientSide())
            GravityChangerAPI.setBaseGravityDirection(user, gravityDirection);
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(
                Component.translatable("gravity_changer.gravity_changer.tooltip.0")
                        .withStyle(ChatFormatting.GRAY)
        );
        tooltipComponents.add(
                Component.translatable("gravity_changer.gravity_changer.tooltip.1")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}
