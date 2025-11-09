package dev.khanhtimn.gravitychanger.network.payload;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.data.attachments.GravityData;
import dev.khanhtimn.gravitychanger.network.GravityNetwork;
import dev.khanhtimn.gravitychanger.network.ServerBoundPacket;
import dev.khanhtimn.gravitychanger.util.GCUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateGravitySyncStatePacket(UUID entityUUID) implements ServerBoundPacket {

    public static final CustomPacketPayload.Type<UpdateGravitySyncStatePacket> TYPE =
            GravityNetwork.ModPacketPayload.createType("update_gravity_sync_state");

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateGravitySyncStatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString),
            UpdateGravitySyncStatePacket::entityUUID,
            UpdateGravitySyncStatePacket::new
    );

    @Override
    public void handleOnServer(ServerPlayer player) {
        Entity entity = GCUtil.getEntityByUUID(player.level(), entityUUID);
        if (entity != null) {
            GravityData data = GravityChangerAPI.getGravityData(entity);
            data.needsSync = false;
            data.noAnimation = false;
        }
    }

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
