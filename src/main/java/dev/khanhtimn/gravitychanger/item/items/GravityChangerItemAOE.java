package dev.khanhtimn.gravitychanger.item.items;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GravityChangerItemAOE extends Item {


    public final Direction gravityDirection;

    public GravityChangerItemAOE(Properties settings, Direction _gravityDirection) {
        super(settings);
        gravityDirection = _gravityDirection;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        if (!world.isClientSide()) {
            AABB box = user.getBoundingBox().inflate(3);
            List<Entity> list = world.getEntitiesOfClass(Entity.class, box, e -> !(e instanceof Player));
            for (Entity entity : list) {
                GravityChangerAPI.setBaseGravityDirection(entity, gravityDirection);
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
