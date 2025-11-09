package dev.khanhtimn.gravitychanger.mixin.compat;

import com.bawnorton.mixinsquared.TargetHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//https://github.com/Tfarcenim/GravityChanger/issues/2
@Mixin(value = Entity.class, priority = 1500)
public class ACEntityMixin {
//    @Restriction(
//            require = {
//                    @Condition("alexcaves")
//            }
//    )
//    @TargetHandler(mixin = "com.github.alexmodguy.alexscaves.mixin.EntityMixin", name = "ac_collide")
//    @Inject(method = "@MixinSquared:Handler", at = @At(value = "HEAD"))
//    private void inject_adjustMovementForCollisions(Vec3 vec3, CallbackInfoReturnable<Vec3> cir, CallbackInfo ci) {
//        //TODO find which mixin is cause problem instead of cancel entirely
//        //com.min01.gravitychanger.mixin.EntityMixin;
//        ci.cancel();
//    }
}
