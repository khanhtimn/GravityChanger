package dev.khanhtimn.gravitychanger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownExperienceBottle.class)
public class ThrownExperienceBottleMixin {
    @ModifyReturnValue(method = "getDefaultGravity", at = @At("RETURN"))
    private double multiplyGravity(double original) {
        return original * GravityChangerAPI.getGravityStrength(((Entity) (Object) this));
    }
}
