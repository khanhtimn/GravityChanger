package dev.khanhtimn.gravitychanger.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @WrapOperation(
            method = "createProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getX()D",
                    ordinal = 0
            )
    )
    private double redirect_shoot_getX_0(LivingEntity livingEntity, Operation<Double> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(livingEntity);
        if (gravityDirection == Direction.DOWN) {
            return livingEntity.getX();
        }

        return livingEntity.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15F, 0.0D, gravityDirection)).x;
    }

    @WrapOperation(
            method = "createProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getEyeY()D",
                    ordinal = 0
            )
    )
    private double redirect_shoot_getEyeY_0(LivingEntity livingEntity, Operation<Double> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(livingEntity);
        if (gravityDirection == Direction.DOWN) {
            return livingEntity.getEyeY();
        }

        return livingEntity.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15F, 0.0D, gravityDirection)).y + 0.15F;
    }

    @WrapOperation(
            method = "createProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getZ()D",
                    ordinal = 0
            )
    )
    private double redirect_shoot_getZ_0(LivingEntity livingEntity, Operation<Double> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(livingEntity);
        if (gravityDirection == Direction.DOWN) {
            return livingEntity.getZ();
        }

        return livingEntity.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15F, 0.0D, gravityDirection)).z;
    }


    @WrapOperation(
            method = "shootProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;getProjectileShotVector(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/phys/Vec3;F)Lorg/joml/Vector3f;"
            )
    )
    private static Vector3f redirect_shoot(LivingEntity shooter, Vec3 distance, float angle, Operation<Vector3f> original, @Local(argsOnly = true) Projectile projectile, @Local(ordinal = 1, argsOnly = true) LivingEntity target) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(shooter);
        if (gravityDirection == Direction.DOWN) {
            return original.call(shooter, distance, angle);
        }

        Vec3 targetPos = target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getBbHeight() * 0.3333333333333333D, 0.0D, gravityDirection));

        double d = targetPos.x() - shooter.getX();
        double e = targetPos.z() - shooter.getZ();

        //twice to aim for the middle of the body, once to aim above,
        // should aim above for UP and DOWN, and middle for NESW
        // maybe should be changed to aim below for UP, (player eye level)
        // but without this for UP they will never hit you (aim below target's feet, aka aims too high)
        // TODO: Figure out how to alter aim for UP direction
        double f = Math.sqrt(d * d + e * e);
        if(gravityDirection != Direction.UP)
            f = Math.sqrt(f);

        double g = targetPos.y - projectile.getY() + f * 0.2F;
        return original.call(shooter, new Vec3(d, g, e), angle);
    }
}
