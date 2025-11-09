package dev.khanhtimn.gravitychanger.mob_effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GravityStrengthMobEffect extends MobEffect {

    public final double base;
    public final int signum;

    public GravityStrengthMobEffect(int color, double base, int signum) {
        super(MobEffectCategory.NEUTRAL, color);
        this.base = base;
        this.signum = signum;
    }

    public double getGravityStrengthMultiplier(int level) {
        return Math.pow(base, level) * signum;
    }
}
