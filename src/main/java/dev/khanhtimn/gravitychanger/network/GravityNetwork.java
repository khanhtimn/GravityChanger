package dev.khanhtimn.gravitychanger.network;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.network.payload.UpdateGravityDataPacket;
import dev.khanhtimn.gravitychanger.network.payload.UpdateGravitySyncStatePacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class GravityNetwork {

    public static void init(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(GravityChanger.MODID);

        // Register client-bound packets
        clientBound(registrar, UpdateGravityDataPacket.TYPE, UpdateGravityDataPacket.CODEC);

        // Register server-bound packets
        serverBound(registrar, UpdateGravitySyncStatePacket.TYPE, UpdateGravitySyncStatePacket.CODEC);
    }

    private static <T extends ClientBoundPacket> void clientBound(PayloadRegistrar registrar,
                                                                  CustomPacketPayload.Type<T> type,
                                                                  StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToClient(type, codec, ClientBoundPacket::handleOnClient);
    }

    private static <T extends ServerBoundPacket> void serverBound(PayloadRegistrar registrar,
                                                                  CustomPacketPayload.Type<T> type,
                                                                  StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        registrar.playToServer(type, codec, ServerBoundPacket::handleOnServer);
    }

    public interface ModPacketPayload extends CustomPacketPayload {
        static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> createType(String name) {
            return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GravityChanger.MODID, name));
        }
    }
}
