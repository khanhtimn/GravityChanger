package dev.khanhtimn.gravitychanger.mixin;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrowableProjectile.class)
public abstract class ThrowableProjectileMixin {


    /*@Override
    public Direction gravitychanger$getAppliedGravityDirection() {
        return GravityChangerAPI.getGravityDirection((ThrownEntity)(Object)this);
    }*/

    @Shadow
    protected abstract double getDefaultGravity();

    @ModifyVariable(
            method = "tick()V",
            at = @At(
                    value = "STORE"
            )
            , ordinal = 0
    )
    public Vec3 tick(Vec3 modify) {
        //if(this instanceof RotatableEntityAccessor) {
        modify = new Vec3(modify.x, modify.y + this.getDefaultGravity(), modify.z);
        modify = RotationUtil.vecWorldToPlayer(modify, GravityChangerAPI.getGravityDirection((ThrowableProjectile) (Object) this));
        modify = new Vec3(modify.x, modify.y - this.getDefaultGravity(), modify.z);
        modify = RotationUtil.vecPlayerToWorld(modify, GravityChangerAPI.getGravityDirection((ThrowableProjectile) (Object) this));
        // }
        return modify;
    }
    
    /*@WrapOperation(
        method = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;<init>(Lnet/minecraft/world/entity/EntityType;DDDLnet/minecraft/world/level/Level;)V",
            ordinal = 0
        )
    )
    private static ThrowableProjectile modifyargs_init_init_0(ThrowableProjectile instance, EntityType<? extends ThrowableProjectile> type, double x, double y, double z, Level world, Operation<ThrowableProjectile> original, @Local LivingEntity owner) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(owner);
        if (gravityDirection == Direction.DOWN) return original.call(instance, type, x, y, z, world);
        
        Vec3 pos = owner.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.10000000149011612D, 0.0D, gravityDirection));
        return original.call(instance, type, pos.x, pos.y, pos.z, world);
    }*/

    @ModifyReturnValue(method = "getDefaultGravity", at = @At("RETURN"))
    private double multiplyGravity(double original) {
        return original * GravityChangerAPI.getGravityStrength(((Entity) (Object) this));
    }
}
