package dev.khanhtimn.gravitychanger.network.payload;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.network.ClientBoundPacket;
import dev.khanhtimn.gravitychanger.network.GravityNetwork;
import dev.khanhtimn.gravitychanger.util.GCUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateGravityDataPacket(
        boolean noAnimation,
        UUID entityUUID,
        Direction baseGravityDirection,
        Direction currentGravityDirection,
        double baseGravityStrength,
        double currentGravityStrength
) implements ClientBoundPacket {

    public static final CustomPacketPayload.Type<UpdateGravityDataPacket> TYPE =
            GravityNetwork.ModPacketPayload.createType("update_gravity_data");

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateGravityDataPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UpdateGravityDataPacket::noAnimation,
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString),
            UpdateGravityDataPacket::entityUUID,
            Direction.STREAM_CODEC,
            UpdateGravityDataPacket::baseGravityDirection,
            Direction.STREAM_CODEC,
            UpdateGravityDataPacket::currentGravityDirection,
            ByteBufCodecs.DOUBLE,
            UpdateGravityDataPacket::baseGravityStrength,
            ByteBufCodecs.DOUBLE,
            UpdateGravityDataPacket::currentGravityStrength,
            UpdateGravityDataPacket::new
    );

    @Override
    public void handleOnClient(Player player) {
        GCUtil.getClientLevel(level -> {
            Entity entity = GCUtil.getEntityByUUID(level, entityUUID);
            if (entity != null) {
                GravityChangerAPI.getGravityData(entity).sync(
                        noAnimation,
                        baseGravityDirection,
                        currentGravityDirection,
                        baseGravityStrength,
                        currentGravityStrength
                );
            }
        });
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
