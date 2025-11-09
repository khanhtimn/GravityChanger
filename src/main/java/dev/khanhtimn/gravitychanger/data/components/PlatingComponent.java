package dev.khanhtimn.gravitychanger.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlatingComponent(boolean isAttracting, int level) {
    public static final Codec<PlatingComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("isAttracting").forGetter(PlatingComponent::isAttracting),
                    Codec.INT.fieldOf("level").forGetter(PlatingComponent::level)
            ).apply(instance, PlatingComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlatingComponent> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    PlatingComponent::isAttracting,
                    ByteBufCodecs.VAR_INT,
                    PlatingComponent::level,
                    PlatingComponent::new
            );
}
