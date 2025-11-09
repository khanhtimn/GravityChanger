package dev.khanhtimn.gravitychanger.mob_effect;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.mob_effect.effects.GravityDirectionMobEffect;
import dev.khanhtimn.gravitychanger.mob_effect.effects.GravityInvertMobEffect;
import dev.khanhtimn.gravitychanger.mob_effect.effects.GravityStrengthMobEffect;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GravityMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, GravityChanger.MODID);

    public static final Holder<MobEffect> DOWN = EFFECTS.register("down", () -> new GravityDirectionMobEffect(Direction.DOWN));
    public static final Holder<MobEffect> UP = EFFECTS.register("up", () -> new GravityDirectionMobEffect(Direction.UP));
    public static final Holder<MobEffect> NORTH = EFFECTS.register("north", () -> new GravityDirectionMobEffect(Direction.NORTH));
    public static final Holder<MobEffect> SOUTH = EFFECTS.register("south", () -> new GravityDirectionMobEffect(Direction.SOUTH));
    public static final Holder<MobEffect> WEST = EFFECTS.register("west", () -> new GravityDirectionMobEffect(Direction.WEST));
    public static final Holder<MobEffect> EAST = EFFECTS.register("east", () -> new GravityDirectionMobEffect(Direction.EAST));
    public static final Holder<MobEffect> INVERT = EFFECTS.register("invert", GravityInvertMobEffect::new);
    public static final Holder<MobEffect> INCREASE = EFFECTS.register("increase", () -> new GravityStrengthMobEffect(0x98D982, 1.2, 1));
    public static final Holder<MobEffect> DECREASE = EFFECTS.register("decrease", () -> new GravityStrengthMobEffect(0x98D982, 0.7, 1));
    public static final Holder<MobEffect> REVERSE = EFFECTS.register("reverse", () -> new GravityStrengthMobEffect(0x98D982, 1.0, -1));

}
