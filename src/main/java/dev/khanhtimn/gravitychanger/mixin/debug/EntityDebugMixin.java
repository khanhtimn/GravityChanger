package dev.khanhtimn.gravitychanger.mixin.debug;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityDebugMixin {

    @Inject(method = "setPos(DDD)V", at = @At("HEAD"))
    private void inject_setPos(double x, double y, double z, CallbackInfo ci) {
        Entity this_ = (Entity) (Object) this;
        if (this_ instanceof ItemEntity) {
            String str = "%s ItemEntity#setPosRaw(%s, %s, %s) grav %s %s".formatted(
                    this_.level().isClientSide() ? "client" : "server", x, y, z,
                    GravityChangerAPI.getGravityDirection(this_),
                    GravityChangerAPI.getGravityStrength(this_)
            );
            GravityChanger.LOGGER.info(str);
        }
    }
}
