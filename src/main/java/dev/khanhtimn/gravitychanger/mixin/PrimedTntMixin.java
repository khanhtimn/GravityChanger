package dev.khanhtimn.gravitychanger.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PrimedTnt.class)
public abstract class PrimedTntMixin extends Entity {
    public PrimedTntMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "getDefaultGravity",
            at = @At("RETURN")
    )
    private double multiplyGravity(double original) {
        return original * GravityChangerAPI.getGravityStrength(((Entity) (Object) this));
    }
}
