package dev.khanhtimn.gravitychanger.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.khanhtimn.gravitychanger.RotationAnimation;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private Camera mainCamera;

    @Inject(method = "renderLevel", at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;rotation(Lorg/joml/Quaternionfc;)Lorg/joml/Matrix4f;",
            shift = At.Shift.BY, by = 2, remap = false
        )
    )
    private void renderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local(ordinal = 1) Matrix4f matrix4f2) {
        if (this.mainCamera.getEntity() != null) {
            Entity focusedEntity = this.mainCamera.getEntity();
            RotationAnimation animation = GravityChangerAPI.getRotationAnimation(focusedEntity);
            // Only override vanilla when a gravity transform/animation is active
            if (animation == null) {
                return;
            }
            if (animation.isInAnimation()) {
                // make sure that frustum culling updates when running rotation animation
                Minecraft.getInstance().levelRenderer.needsUpdate();
            }
        }
    }
}
