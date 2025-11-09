package dev.khanhtimn.gravitychanger;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GravityConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue rotationTime = BUILDER
            .comment("animation rotation time").defineInRange("rotationTime", 500, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue gravityStrengthMultiplier = BUILDER
            .comment("gravity strength multiplier").defineInRange("gravityStrengthMultiplier", 1.0F, 0.0F, Float.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue  worldVelocity = BUILDER
            .comment("world velocity").define("worldVelocity", false);

    public static final ModConfigSpec.BooleanValue  resetGravityOnRespawn = BUILDER
            .comment("reset gravity on respawn").define("resetGravityOnRespawn", true);

    public static final ModConfigSpec.BooleanValue  voidDamageAboveWorld = BUILDER
            .comment("void damage when above world").define("voidDamageAboveWorld", true);

    public static final ModConfigSpec.BooleanValue  voidDamageOnHorizontalFallTooFar = BUILDER
            .comment("void damage when horizontally fall too far").define("voidDamageOnHorizontalFallTooFar", true);

    public static final ModConfigSpec.BooleanValue  autoJumpOnGravityPlateInnerCorner = BUILDER
            .comment("auto jump on gravity plate inner corner").define("autoJumpOnGravityPlateInnerCorner", true);

    public static final ModConfigSpec.BooleanValue  adjustPositionAfterChangingGravity = BUILDER
            .comment("adjust position after gravity change").define("adjustPositionAfterChangingGravity", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
