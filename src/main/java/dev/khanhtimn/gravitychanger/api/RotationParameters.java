package dev.khanhtimn.gravitychanger.api;

import dev.khanhtimn.gravitychanger.GravityChangerConfig;
import net.minecraft.nbt.CompoundTag;

public record RotationParameters(
        boolean rotateVelocity,
        boolean rotateView, // currently ignores this
        int rotationTimeMS
) {
    public static RotationParameters defaultParam = new RotationParameters(
            true, true, 500
    );

    public static void updateDefault() {
        defaultParam = new RotationParameters(
                !GravityChangerConfig.worldVelocity.get(),
                true,
                GravityChangerConfig.rotationTime.get()
        );
    }

    public static RotationParameters getDefault() {
        return defaultParam;
    }

    public static RotationParameters fromTag(CompoundTag tag) {
        return new RotationParameters(
                tag.getBoolean("RotateVelocity"),
                tag.getBoolean("RotateView"),
                tag.getInt("RotationTimeMS")
        );
    }

    public RotationParameters withRotationTimeMs(int rotationTimeMS) {
        return new RotationParameters(
                rotateVelocity,
                rotateView,
                rotationTimeMS
        );
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("RotateVelocity", rotateVelocity);
        tag.putBoolean("RotateView", rotateView);
        tag.putInt("RotationTimeMS", rotationTimeMS);
        return tag;
    }
}
