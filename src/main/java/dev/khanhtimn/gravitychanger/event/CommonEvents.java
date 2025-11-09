package dev.khanhtimn.gravitychanger.event;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.api.GravityChangerAPI;
import dev.khanhtimn.gravitychanger.data.attachments.GravityData;
import dev.khanhtimn.gravitychanger.GravityConfig;
import dev.khanhtimn.gravitychanger.command.GravityCommand;
import dev.khanhtimn.gravitychanger.util.GCUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Objects;

public class CommonEvents {
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        GravityCommand.register(event.getDispatcher());
    }

    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        GCUtil.ENTITY_MAP.put(entity.getClass().hashCode(), entity);
        GCUtil.ENTITY_MAP2.put(entity.getClass().getSuperclass().hashCode(), entity);
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (event.isWasDeath() && !GravityConfig.resetGravityOnRespawn.get()) {
            Player original = event.getOriginal();
            original.revive();
            GravityChangerAPI.setBaseGravityDirection(player, GravityChangerAPI.getBaseGravityDirection(original));
        }
        for (Entity entity : Objects.requireNonNull(GCUtil.getAllEntities(player.level()))) {
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
