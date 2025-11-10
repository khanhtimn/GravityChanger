package dev.khanhtimn.gravitychanger.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = Player.class, priority = 1001)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    @Final
    private Abilities abilities;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean isStayingOnGroundSurface();

    @Shadow
    protected abstract boolean isAboveGround(float maxUpStep);

    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getLookAngle()Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 wrapOperation_travel_getRotationVector_0(Player playerEntity, Operation<Vec3> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN) {
            return original.call(playerEntity);
        }

        return RotationUtil.vecWorldToPlayer(original.call(playerEntity), gravityDirection);
    }


    @ModifyArgs(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;containing(DDD)Lnet/minecraft/core/BlockPos;"
            )
    )
    private void modify_move_multiply_0(Args args) {
        Vec3 rotate = new Vec3(0.0D, 1.0D - 0.1D, 0.0D);
        rotate = RotationUtil.vecPlayerToWorld(rotate, GravityChangerAPI.getGravityDirection(this));
        args.set(0, (double) args.get(0) - rotate.x);
        args.set(1, (double) args.get(1) - rotate.y + (1.0D - 0.1D));
        args.set(2, (double) args.get(2) - rotate.z);
    }

    @WrapOperation(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;",
                    ordinal = 0
            )
    )
    private ItemEntity redirect_dropItem_new_0(
            Level level, double posX, double posY, double posZ, ItemStack itemStack, Operation<ItemEntity> original
    ) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return original.call(level, posX, posY, posY, itemStack);
        }

        Vec3 vec3d = this.getEyePosition()
                .subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.3D, 0.0D, gravityDirection));

        ItemEntity itemEntity = new ItemEntity(level, vec3d.x, vec3d.y, vec3d.z, itemStack);

        // change the gravity of the thrown item
        GravityChangerAPI.setBaseGravityDirection(
            itemEntity, gravityDirection
        );
        // the item entity calculates position both on client and server separately
        // if gravity is not down, the client and server will desync (the reason is not yet known)
        // don't let item change gravity for now

        return itemEntity;
    }

    @WrapOperation(
            method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            )
    )
    private void wrapOperation_dropItem_setVelocity(ItemEntity itemEntity, double x, double y, double z, Operation<Void> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            original.call(itemEntity, x, y, z);
            return;
        }

        Vec3 world = RotationUtil.vecPlayerToWorld(x, y, z, gravityDirection);
        GravityChangerAPI.setWorldVelocity(itemEntity, world);
    }

//    @Inject(
//            method = "maybeBackOffFromEdge(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/MoverType;)Lnet/minecraft/world/phys/Vec3;",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void inject_adjustMovementForSneaking(Vec3 movement, MoverType type, CallbackInfoReturnable<Vec3> cir) {
//        Entity this_ = (Entity) (Object) this;
//        Direction gravityDirection = GravityChangerAPI.getGravityDirection(this_);
//        if (gravityDirection == Direction.DOWN) return;
//
//        float f = this_.maxUpStep();
//        Vec3 playerMovement = RotationUtil.vecWorldToPlayer(movement, gravityDirection);
//        if (!this.abilities.flying && (type == MoverType.SELF || type == MoverType.PLAYER) && this.isStayingOnGroundSurface() && this.isAboveGround(f)) {
//            double d = playerMovement.x;
//            double e = playerMovement.z;
//            double var7 = 0.05D;
//
//            while (d != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(d, (double) (-this.maxUpStep()), 0.0D, gravityDirection)))) {
//                if (d < 0.05D && d >= -0.05D) {
//                    d = 0.0D;
//                } else if (d > 0.0D) {
//                    d -= 0.05D;
//                } else {
//                    d += 0.05D;
//                }
//            }
//
//            while (e != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(0.0D, (double) (-this.maxUpStep()), e, gravityDirection)))) {
//                if (e < 0.05D && e >= -0.05D) {
//                    e = 0.0D;
//                } else if (e > 0.0D) {
//                    e -= 0.05D;
//                } else {
//                    e += 0.05D;
//                }
//            }
//
//            while (d != 0.0D && e != 0.0D && this_.level().noCollision(this, this.getBoundingBox().move(RotationUtil.vecPlayerToWorld(d, (double) (-this.maxUpStep()), e, gravityDirection)))) {
//                if (d < 0.05D && d >= -0.05D) {
//                    d = 0.0D;
//                } else if (d > 0.0D) {
//                    d -= 0.05D;
//                } else {
//                    d += 0.05D;
//                }
//
//                if (e < 0.05D && e >= -0.05D) {
//                    e = 0.0D;
//                } else if (e > 0.0D) {
//                    e -= 0.05D;
//                } else {
//                    e += 0.05D;
//                }
//            }
//
//            cir.setReturnValue(RotationUtil.vecPlayerToWorld(d, playerMovement.y, e, gravityDirection));
//        } else {
//            cir.setReturnValue(movement);
//        }
//    }


    @ModifyVariable(
            method = "maybeBackOffFromEdge",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private Vec3 injected(Vec3 movement) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        return RotationUtil.vecWorldToPlayer(movement, gravityDirection);
    }

    @Inject(
            method = "maybeBackOffFromEdge",
            at = @At(value = "RETURN", ordinal = 0),
            cancellable = true
    )
    private void modify_return_if(CallbackInfoReturnable<Vec3> cir,
                                  @Local(argsOnly = true) Vec3 movement,
                                  @Local(ordinal = 0) double d0,
                                  @Local(ordinal = 1) double d1) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        cir.setReturnValue(RotationUtil.vecPlayerToWorld(d0, movement.y, d1, gravityDirection));
    }

    @Inject(
            method = "maybeBackOffFromEdge",
            at = @At(value = "RETURN", ordinal = 1),
            cancellable = true
    )
    private void modify_return_else(CallbackInfoReturnable<Vec3> cir, @Local(argsOnly = true) Vec3 movement) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        cir.setReturnValue(RotationUtil.vecPlayerToWorld(movement, gravityDirection));
    }

    @Redirect(
            method = "canFallAtLeast",
            at = @At(
                    value = "NEW",
                    target = "(DDDDDD)Lnet/minecraft/world/phys/AABB;")
    )
    private AABB redirect_canFallAtLeast_new_0(
            double x1, double y1, double z1, double x2, double y2, double z2,
            @Local(ordinal = 0, argsOnly = true) double offsetX,
            @Local(ordinal = 1, argsOnly = true) double offsetZ,
            @Local(ordinal = 0, argsOnly = true) float offsetY,
            @Local(ordinal=0) AABB aabb
    ) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        double margin = 1.0E-7;
        Vec3 offsets = RotationUtil.vecPlayerToWorld(offsetX, -(offsetY + margin * 2), offsetZ, gravityDirection);
        return new AABB(
                aabb.minX + offsets.x + margin, aabb.minY + offsets.y + margin, aabb.minZ + offsets.z + margin,
                aabb.maxX + offsets.x - margin, aabb.maxY + offsets.y - margin, aabb.maxZ + offsets.z - margin);
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getYRot()F",
                    ordinal = 0
            )
    )
    private float wrapOperation_attack_getYaw_0(Player attacker, Operation<Float> original, Entity target) {
        Direction targetGravityDirection = GravityChangerAPI.getGravityDirection(target);
        Direction attackerGravityDirection = GravityChangerAPI.getGravityDirection(attacker);
        if (targetGravityDirection == attackerGravityDirection) {
            return original.call(attacker);
        }

        return RotationUtil.rotWorldToPlayer(RotationUtil.rotPlayerToWorld(original.call(attacker), attacker.getXRot(), attackerGravityDirection), targetGravityDirection).x;
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getYRot()F",
                    ordinal = 1
            )
    )
    private float wrapOperation_attack_getYaw_1(Player attacker, Operation<Float> original, Entity target) {
        Direction targetGravityDirection = GravityChangerAPI.getGravityDirection(target);
        Direction attackerGravityDirection = GravityChangerAPI.getGravityDirection(attacker);
        if (targetGravityDirection == attackerGravityDirection) {
            return original.call(attacker);
        }

        return RotationUtil.rotWorldToPlayer(RotationUtil.rotPlayerToWorld(original.call(attacker), attacker.getXRot(), attackerGravityDirection), targetGravityDirection).x;
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getYRot()F",
                    ordinal = 2
            )
    )
    private float wrapOperation_attack_getYaw_2(Player attacker, Operation<Float> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(attacker);
        if (gravityDirection == Direction.DOWN) {
            return original.call(attacker);
        }

        return RotationUtil.rotPlayerToWorld(original.call(attacker), attacker.getXRot(), gravityDirection).x;
    }

    @WrapOperation(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getYRot()F",
                    ordinal = 3
            )
    )
    private float wrapOperation_attack_getYaw_3(Player attacker, Operation<Float> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(attacker);
        if (gravityDirection == Direction.DOWN) {
            return original.call(attacker);
        }

        return RotationUtil.rotPlayerToWorld(original.call(attacker), attacker.getXRot(), gravityDirection).x;
    }

    //TODO: Rotate Death Particles
//    @WrapOperation(
//            method = "addParticlesAroundSelf",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
//            )
//    )
//    private void modify_addDeathParticless_addParticle_0(Level instance, ParticleOptions particle, double x, double y, double z, double dx, double dy, double dz, Operation<Void> original) {
//        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
//
//        if (gravityDirection == Direction.DOWN) {
//            original.call(instance, particle, x, y, z, dx, dy, dz);
//        } else {
//            Vec3 vec3d = this.position().subtract(RotationUtil.vecPlayerToWorld(this.position().subtract(x, y, z), gravityDirection));
//            original.call(instance, particle, vec3d.x, vec3d.y, vec3d.z, dx, dy, dz);
//        }
//    }

//    @WrapOperation(
//            method = "aiStep",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
//            )
//    )
//    private AABB modify_tickMovement_expand_0(AABB instance, double x, double y, double z, Operation<AABB> original) {
//        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
//        if (gravityDirection == Direction.DOWN) return original.call(instance, x, y, z);
//
//        Vec3 vec3d = RotationUtil.maskPlayerToWorld(x, y, z, gravityDirection);
//        return original.call(instance, vec3d.x, vec3d.y, vec3d.z);
//    }


    @ModifyArgs(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            )
    )
    private void modify_tickMovement_expand_0(Args args) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;

        Vec3 vec3d = RotationUtil.maskPlayerToWorld(args.get(0), args.get(1), args.get(2), gravityDirection);
        args.set(0, vec3d.x);
        args.set(1, vec3d.y);
        args.set(2, vec3d.z);
    }


    @WrapOperation(
            method = "canPlayerFitWithinBlocksAndEntitiesWhen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityDimensions;makeBoundingBox(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/AABB;"
            )
    )
    private AABB wrapOperation_canChangeIntoPose_getBoundingBox(EntityDimensions dimensions, Vec3 pos, Operation<AABB> original) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) {
            return original.call(dimensions, pos);
        }

        AABB box = dimensions.makeBoundingBox(0, 0, 0);
        //Box box = original.call(dimensions, pos).offset(pos.negate());
        if (gravityDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            box = box.move(0.0D, -1.0E-6D, 0.0D);
        }
        return RotationUtil.boxPlayerToWorld(box, gravityDirection).move(pos);

    }
}
