package dev.khanhtimn.gravitychanger.item;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.data.components.PlatingComponent;
import dev.khanhtimn.gravitychanger.item.items.CreativeTabIconItem;
import dev.khanhtimn.gravitychanger.item.items.GravityPlatingItem;
import dev.khanhtimn.gravitychanger.mob_effect.GravityPotions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GravityCreativeTabs {

    public static final DeferredItem<Item> CREATIVE_TAB_ICON = GravityItems.ITEMS.register("creative_tab_icon",
            () -> new CreativeTabIconItem(new Item.Properties().stacksTo(1).durability(0)));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GravityChanger.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRAVITYCHANGER = CREATIVE_MODE_TAB.register("general", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gravitychanger"))
            .icon(() -> new ItemStack(CREATIVE_TAB_ICON.asItem()))
            .displayItems((enabledFeatures, output) -> {

                GravityItems.ITEMS.getEntries().stream()
                        .map(DeferredHolder::get)
                        .filter(item -> item != CREATIVE_TAB_ICON.get().asItem() && item != GravityItems.GRAVITY_PLATING.get().asItem())
                        .map(ItemStack::new)
                        .forEach(output::accept);

                List.of(1, 2, 8, 32, 64).forEach(amount ->
                        output.accept(GravityPlatingItem.createStack(new PlatingComponent(true, amount))));
                List.of(8, 32).forEach(amount ->
                        output.accept(GravityPlatingItem.createStack(new PlatingComponent(false, amount))));

                GravityPotions.POTIONS.getEntries().forEach(reg -> {
                    output.accept(PotionContents.createItemStack(Items.POTION, reg.getDelegate()));
                    output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, reg.getDelegate()));
                    output.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, reg.getDelegate()));
                });
            }).build());
}
