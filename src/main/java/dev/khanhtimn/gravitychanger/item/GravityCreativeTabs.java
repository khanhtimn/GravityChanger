package dev.khanhtimn.gravitychanger.item;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.data.components.PlatingComponent;
import dev.khanhtimn.gravitychanger.item.items.GravityPlatingItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GravityCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GravityChanger.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRAVITYCHANGER = CREATIVE_MODE_TAB.register(GravityChanger.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gravitychanger"))
            .icon(() -> new ItemStack(GravityItems.GRAVITY_CHANGER_UP.get()))
            .displayItems((enabledFeatures, output) ->
            {
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_UP.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_DOWN.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_EAST.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_WEST.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_NORTH.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_SOUTH.get()));

                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_UP_AOE.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_DOWN_AOE.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_EAST_AOE.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_WEST_AOE.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_NORTH_AOE.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_CHANGER_SOUTH_AOE.get()));

                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(true, 1)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(true, 2)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(true, 8)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(true, 32)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(true, 64)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(false, 8)
                ));
                output.accept(GravityPlatingItem.createStack(
                        new PlatingComponent(false, 32)
                ));

                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_UP.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_DOWN.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_EAST.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_WEST.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_NORTH.get()));
                output.accept(new ItemStack(GravityItems.GRAVITY_ANCHOR_SOUTH.get()));
            }).build());
}
