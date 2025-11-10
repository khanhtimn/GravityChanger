package dev.khanhtimn.gravitychanger.event;

import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.data.attachments.GravityData;
import dev.khanhtimn.gravitychanger.GravityChangerConfig;
import dev.khanhtimn.gravitychanger.command.GravityCommand;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CommonEvents {
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        GravityCommand.register(event.getDispatcher());
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (event.isWasDeath() && !GravityChangerConfig.resetGravityOnRespawn.get()) {
            Player original = event.getOriginal();
            original.revive();
            GravityChangerAPI.setBaseGravityDirection(player, GravityChangerAPI.getBaseGravityDirection(original));
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (!entity.level().isClientSide) {
                    if (GravityChangerAPI.getBaseGravityDirection(entity) == Direction.DOWN) {
                        continue;
                    }
                    GravityData data = GravityChangerAPI.getGravityData(entity);
                    data.initialized = false;
                    data.initialize(entity);
                }
            }
        }
    }
}
