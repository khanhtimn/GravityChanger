package dev.khanhtimn.gravitychanger.api;

import dev.khanhtimn.gravitychanger.EntityTags;
import dev.khanhtimn.gravitychanger.RotationAnimation;
import dev.khanhtimn.gravitychanger.data.GravityAttachedData;
import dev.khanhtimn.gravitychanger.data.attachments.GravityData;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GravityChangerAPI {
    /**
     * Returns the applied gravity direction for the given entity
     */
    public static Direction getGravityDirection(Entity entity) {
        GravityData data = getGravityData(entity);

        return data.getCurrGravityDirection();
    }

    public static double getGravityStrength(Entity entity) {
        return getGravityData(entity).getCurrGravityStrength();
    }

    public static double getBaseGravityStrength(Entity entity) {
        return getGravityData(entity).getBaseGravityStrength();
    }

    public static void setBaseGravityStrength(Entity entity, double strength) {
        GravityData data = getGravityData(entity);
        data.setBaseGravityStrength(strength);
    }

    public static void resetGravity(Entity entity) {
        if (!EntityTags.canChangeGravity(entity)) {
            return;
        }

        getGravityData(entity).reset();
    }

    /**
     * Returns the main gravity direction for the given entity
     * This may not be the applied gravity direction for the player, see GravityChangerAPI#getAppliedGravityDirection
     */
    public static Direction getBaseGravityDirection(Entity entity) {
        return getGravityData(entity).getBaseGravityDirection();
    }

    public static void setBaseGravityDirection(
            Entity entity, Direction gravityDirection
    ) {
        GravityData data = getGravityData(entity);
        data.setBaseGravityDirection(gravityDirection);
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static RotationAnimation getRotationAnimation(Entity entity) {
        return getGravityData(entity).getRotationAnimation();
    }

    /**
     * Instantly set gravity direction on client side without performing animation.
     * Not needed in normal cases.
     * (Used by iPortal)
     */
    public static void instantlySetClientBaseGravityDirection(Entity entity, Direction direction) {
        Validate.isTrue(entity.level().isClientSide(), "should only be used on client");

        GravityData data = getGravityData(entity);

        data.setBaseGravityDirection(direction);

        data.updateGravityStatus(false);

        data.forceApplyGravityChange();
    }

    public static GravityData getGravityData(Entity entity) {
        GravityData data = entity.getData(GravityAttachedData.GRAVITY_DATA);
        if (!data.initialized) {
            data.initialize(entity);
        }
        return data;
    }

    public static @NotNull GravityData getGravityDataEarly(Entity entity) {
        return getGravityData(entity);
    }

    /**
     * Returns the world relative velocity for the given entity
     * Using minecraft's methods to get the velocity will return entity local velocity
     */
    public static Vec3 getWorldVelocity(Entity entity) {
        return RotationUtil.vecPlayerToWorld(entity.getDeltaMovement(), getGravityDirection(entity));
    }

    /**
     * Sets the world relative velocity for the given player
     * Using minecraft's methods to set the velocity of an entity will set player relative velocity
     */
    public static void setWorldVelocity(Entity entity, Vec3 worldVelocity) {
        entity.setDeltaMovement(RotationUtil.vecWorldToPlayer(worldVelocity, getGravityDirection(entity)));
    }

    /**
     * Returns eye position offset from feet position for the given entity
     */
    public static Vec3 getEyeOffset(Entity entity) {
        return RotationUtil.vecPlayerToWorld(0, (double) entity.getEyeHeight(), 0, getGravityDirection(entity));
    }

    public static boolean canChangeGravity(Entity entity) {
        return EntityTags.canChangeGravity(entity);
    }

}
