package dev.khanhtimn.gravitychanger.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.khanhtimn.gravitychanger.GravityChanger;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import qouteall.imm_ptl.core.platform_specific.IPModEntry;

@Restriction(
        require = {
                @Condition("immersive_portals_core")
        }
)
@Mixin(IPModEntry.class)
public abstract class IPModEntryMixin {

    @ModifyExpressionValue(
            method = "onInitialize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/fml/ModList;isLoaded(Ljava/lang/String;)Z",
                    ordinal = 1,
                    remap = false
            )
    )
    private boolean inject_onInitialize(boolean original) {
        return original || ModList.get().isLoaded(GravityChanger.MODID);
    }
}
