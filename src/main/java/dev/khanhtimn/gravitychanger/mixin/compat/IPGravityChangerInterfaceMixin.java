package dev.khanhtimn.gravitychanger.mixin.compat;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.q_misc_util.my_util.DQuaternion;

@Restriction(
        require = {
                @Condition("immersive_portals_core")
        }
)
@Mixin(targets = "qouteall.imm_ptl.core.compat.GravityChangerInterface$OnGravityChangerPresent")
public abstract class IPGravityChangerInterfaceMixin {

    @ModifyReturnValue(
            method = "getEyeOffset",
            at = @At("RETURN"),
            remap = false
    )
    private Vec3 modify_getEyeOffset(Vec3 original, @Local(argsOnly = true) Entity entity) {
        return GravityChangerAPI.getEyeOffset(entity);
    }

    @ModifyReturnValue(
            method = "getGravityDirection",
            at = @At("RETURN"),
            remap = false
    )
    private Direction modify_getGravityDirection(Direction original, @Local(argsOnly = true) Entity entity) {
        return GravityChangerAPI.getGravityDirection(entity);
    }

    @ModifyReturnValue(
            method = "getBaseGravityDirection",
            at = @At("RETURN"),
            remap = false
    )
    private Direction modify_getBaseGravityDirection(Direction original, @Local(argsOnly = true) Entity entity) {
        return GravityChangerAPI.getGravityDirection(entity);
    }

    @Inject(
            method = "setBaseGravityDirectionServer",
            at = @At("HEAD"),
            remap = false
    )
    private void inject_setBaseGravityDirectionServer(Entity entity, Direction direction, CallbackInfo ci) {
        GravityChangerAPI.setBaseGravityDirection(entity, direction);
    }

    @ModifyReturnValue(
            method = "getExtraCameraRotation",
            at = @At("RETURN"),
            remap = false
    )
    private DQuaternion modify_getExtraCameraRotation(DQuaternion original, @Local(argsOnly = true) Direction gravityDirection) {
        if (gravityDirection == Direction.DOWN) {
            return original;
        }

        return DQuaternion.fromMcQuaternion(RotationUtil.getWorldRotationQuaternion(gravityDirection));
    }

    @ModifyReturnValue(
            method = "getWorldVelocity",
            at = @At("RETURN"),
            remap = false
    )
    private Vec3 modify_getWorldVelocity(Vec3 original, @Local(argsOnly = true) Entity entity) {
        return GravityChangerAPI.getWorldVelocity(entity);
    }

    @Inject(
            method = "setWorldVelocity",
            at = @At("HEAD"),
            remap = false
    )
    private void inject_setWorldVelocity(Entity entity, Vec3 newVelocity, CallbackInfo ci) {
        GravityChangerAPI.setWorldVelocity(entity, newVelocity);
    }

    @ModifyReturnValue(
            method = "transformPlayerToWorld",
            at = @At("RETURN"),
            remap = false
    )
    private Vec3 modify_transformPlayerToWorld(Vec3 original, @Local(argsOnly = true) Direction gravity, @Local(argsOnly = true) Vec3 vec3d) {
        return RotationUtil.vecPlayerToWorld(vec3d, gravity);
    }

    @ModifyReturnValue(
            method = "transformWorldToPlayer",
            at = @At("RETURN"),
            remap = false
    )
    private Vec3 modify_transformWorldToPlayer(Vec3 original, @Local(argsOnly = true) Direction gravity, @Local(argsOnly = true) Vec3 vec3d) {
        return RotationUtil.vecWorldToPlayer(vec3d, gravity);
    }

    @ModifyReturnValue(
            method = "transformDirPlayerToWorld",
            at = @At("RETURN"),
            remap = false
    )
    private Direction modify_transformDirPlayerToWorld(Direction original, @Local(argsOnly = true, ordinal = 0) Direction gravity, @Local(argsOnly = true, ordinal = 1) Direction direction) {
        return RotationUtil.dirPlayerToWorld(direction, gravity);
    }

    @ModifyReturnValue(
            method = "transformDirWorldToPlayer",
            at = @At("RETURN"),
            remap = false
    )
    private Direction modify_transformDirWorldToPlayer(Direction original, @Local(argsOnly = true, ordinal = 0) Direction gravity, @Local(argsOnly = true, ordinal = 1) Direction direction) {
        return RotationUtil.dirWorldToPlayer(direction, gravity);
    }
}
