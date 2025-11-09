package dev.khanhtimn.gravitychanger;

import com.mojang.logging.LogUtils;
import dev.khanhtimn.gravitychanger.command.argument.DirectionArgumentType;
import dev.khanhtimn.gravitychanger.command.argument.LocalDirectionArgumentType;
import dev.khanhtimn.gravitychanger.data.GravityAttachedData;
import dev.khanhtimn.gravitychanger.data.GravityComponents;
import dev.khanhtimn.gravitychanger.event.CommonEvents;
import dev.khanhtimn.gravitychanger.block.GravityBlocks;
import dev.khanhtimn.gravitychanger.item.GravityCreativeTabs;
import dev.khanhtimn.gravitychanger.item.GravityItems;
import dev.khanhtimn.gravitychanger.mob_effect.GravityMobEffects;
import dev.khanhtimn.gravitychanger.mob_effect.GravityPotions;
import dev.khanhtimn.gravitychanger.network.GravityNetwork;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(GravityChanger.MODID)
public class GravityChanger {
    public static final String MODID = "gravitychanger";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GravityChanger(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(GravityNetwork::init);
        GravityItems.ITEMS.register(modEventBus);
        GravityBlocks.BLOCKS.register(modEventBus);
        GravityBlocks.BLOCK_ENTITIES.register(modEventBus);
        GravityMobEffects.EFFECTS.register(modEventBus);
        GravityPotions.POTIONS.register(modEventBus);
        GravityCreativeTabs.CREATIVE_MODE_TAB.register(modEventBus);
        GravityAttachedData.DATA_ATTACHMENT_TYPES.register(modEventBus);
        GravityComponents.DATA_COMPONENT_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, GravityChangerConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ArgumentTypeInfos.registerByClass(DirectionArgumentType.class, SingletonArgumentInfo.contextFree(DirectionArgumentType::new));
        ArgumentTypeInfos.registerByClass(LocalDirectionArgumentType.class, SingletonArgumentInfo.contextFree(LocalDirectionArgumentType::new));

        NeoForge.EVENT_BUS.addListener(CommonEvents::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(CommonEvents::onEntityJoinLevel);
    }
}
