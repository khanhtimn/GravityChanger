package dev.khanhtimn.gravitychanger.mob_effect;

import dev.khanhtimn.gravitychanger.GravityChanger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GravityPotions {

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, GravityChanger.MODID);

    public static final Holder<Potion> DOWN_POTION = POTIONS.register("gravity_down_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.DOWN, 9600, 1
            )
    ));

    public static final Holder<Potion> UP_POTION = POTIONS.register("gravity_up_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.UP, 9600, 1
            )
    ));

    public static final Holder<Potion> NORTH_POTION = POTIONS.register("gravity_north_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.NORTH, 9600, 1
            )
    ));

    public static final Holder<Potion> SOUTH_POTION = POTIONS.register("gravity_south_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.SOUTH, 9600, 1
            )
    ));

    public static final Holder<Potion> WEST_POTION = POTIONS.register("gravity_west_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.WEST, 9600, 1
            )
    ));

    public static final Holder<Potion> EAST_POTION = POTIONS.register("gravity_east_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.EAST, 9600, 1
            )
    ));

    public static final Holder<Potion> STRENGTH_INCR_POTION_0 = POTIONS.register("gravity_incr_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.INCREASE, 9600, 0
            )
    ));

    public static final Holder<Potion> STRENGTH_INCR_POTION_1 = POTIONS.register("gravity_incr_1", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.INCREASE, 9600, 1
            )
    ));

    public static final Holder<Potion> STRENGTH_DECR_POTION_0 = POTIONS.register("gravity_decr_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.DECREASE, 9600, 0
            )
    ));
    public static final Holder<Potion> STRENGTH_DECR_POTION_1 = POTIONS.register("gravity_decr_1", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.DECREASE, 9600, 1
            )
    ));

    public static final Holder<Potion> STRENGTH_REVERSE_POTION_0 = POTIONS.register("gravity_reverse_0", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.REVERSE, 9600, 0
            )
    ));

    public static final Holder<Potion> STRENGTH_REVERSE_POTION_1 = POTIONS.register("gravity_reverse_1", () -> new Potion(
            new MobEffectInstance(
                    GravityMobEffects.REVERSE, 9600, 1
            )
    ));
}
