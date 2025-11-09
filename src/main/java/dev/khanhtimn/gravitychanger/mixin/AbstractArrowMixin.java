package dev.khanhtimn.gravitychanger.mixin;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Entity {

    public AbstractArrowMixin(EntityType<?> type, Level world) {
        super(type, world);
    }


    @ModifyVariable(
            method = "tick()V",
            at = @At(
                    value = "STORE"
            )
            , ordinal = 0
    )
    public Vec3 tick(Vec3 modify) {
        modify = new Vec3(modify.x, modify.y + 0.05, modify.z);
        modify = RotationUtil.vecWorldToPlayer(modify, GravityChangerAPI.getGravityDirection(this));
        modify = new Vec3(modify.x, modify.y - 0.05, modify.z);
        modify = RotationUtil.vecPlayerToWorld(modify, GravityChangerAPI.getGravityDirection(this));
        return modify;
    }

    @ModifyReturnValue(
            method = "getDefaultGravity",
            at = @At("RETURN")
    )
    private double multiplyGravity(double original) {
        return original * GravityChangerAPI.getGravityStrength(((Entity) (Object) this));
    }
}
