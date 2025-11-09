package dev.khanhtimn.gravitychanger.mob_effect.effects;

import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.EnumMap;

public class GravityDirectionMobEffect extends MobEffect {
    public static final int COLOR = 0x98D982;

    public static final EnumMap<Direction, GravityDirectionMobEffect> EFFECT_MAP =
            new EnumMap<>(Direction.class);

    static {
        for (Direction dir : Direction.values()) {
            GravityDirectionMobEffect effect = new GravityDirectionMobEffect(dir);
            EFFECT_MAP.put(dir, effect);
        }
    }

    public final Direction gravityDirection;

    public GravityDirectionMobEffect(Direction gravityDirection) {
        super(MobEffectCategory.NEUTRAL, COLOR);
        this.gravityDirection = gravityDirection;
    }
}
