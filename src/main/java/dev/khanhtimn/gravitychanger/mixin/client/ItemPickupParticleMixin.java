package dev.khanhtimn.gravitychanger.mixin.client;


import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.util.RotationUtil;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemPickupParticle.class)
public abstract class ItemPickupParticleMixin {
    @Shadow
    @Final
    private Entity target;

    @Shadow
    private double targetX;

    @Shadow
    private double targetY;

    @Shadow
    private double targetZ;

    /**
     * Make item absorption destination correct.
     * @author qouteall
     * @reason simpler than multiple injections
     */
    @Overwrite
    private void updatePosition() {
        Vec3 entityPos = this.target.position();
        Vec3 eyePos = this.target.getEyePosition();
        Vec3 mid = eyePos.add(entityPos).multiply(0.5, 0.5, 0.5);

        this.targetX = mid.x();
        this.targetY = mid.y();
        this.targetZ = mid.z();
    }
}