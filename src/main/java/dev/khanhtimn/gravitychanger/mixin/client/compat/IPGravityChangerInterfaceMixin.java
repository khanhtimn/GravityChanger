package dev.khanhtimn.gravitychanger.mixin.client.compat;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(
        require = {
                @Condition("immersive_portals_core")
        }
)
@Mixin(targets = "qouteall.imm_ptl.core.compat.GravityChangerInterface$OnGravityChangerPresent")
public abstract class IPGravityChangerInterfaceMixin {

    @Inject(
            method = "setClientPlayerGravityDirectionClientOnly",
            at = @At("HEAD"),
            remap = false
    )
    private void inject_setClientPlayerGravityDirectionClientOnly(Player player, Direction direction, CallbackInfo ci) {
        Validate.isTrue(Minecraft.getInstance().isSameThread());

        GravityChangerAPI.instantlySetClientBaseGravityDirection(player, direction);
    }
}
