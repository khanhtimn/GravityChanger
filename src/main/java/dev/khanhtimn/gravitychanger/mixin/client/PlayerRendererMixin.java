package dev.khanhtimn.gravitychanger.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @ModifyArgs(
            method = "getRenderOffset(Lnet/minecraft/client/player/AbstractClientPlayer;F)Lnet/minecraft/world/phys/Vec3;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"
            )
    )
    private void redirect_getRenderOffset_Vec3d_0(Args args, @Local(argsOnly = true) AbstractClientPlayer instance) {
        Direction gravityDirection = GravityChangerAPI.getGravityDirection(instance);
        if (gravityDirection == Direction.DOWN) {
            return;
        }

        Vec3 offset = new Vec3(args.get(0), args.get(1), args.get(2));
        offset = RotationUtil.vecPlayerToWorld(offset, gravityDirection);
        args.set(0, offset.x);
        args.set(1, offset.y);
        args.set(2, offset.z);
    }


    @WrapOperation(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;getViewVector(F)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 modify_setupTransforms_Vec3d_0(AbstractClientPlayer instance, float partialTick, Operation<Vec3> original) {
        Vec3 viewVector = instance.getViewVector(partialTick);

        Direction gravityDirection = GravityChangerAPI.getGravityDirection(instance);
        if (gravityDirection == Direction.DOWN) {
            return original.call(instance, partialTick);
        }

        return RotationUtil.vecWorldToPlayer(viewVector, gravityDirection);
    }
}
