package dev.khanhtimn.gravitychanger.item;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.block.GravityBlocks;
import dev.khanhtimn.gravitychanger.item.items.GravityAnchorItem;
import dev.khanhtimn.gravitychanger.item.items.GravityChangerItem;
import dev.khanhtimn.gravitychanger.item.items.GravityChangerItemAOE;
import dev.khanhtimn.gravitychanger.item.items.GravityPlatingItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GravityItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GravityChanger.MODID);

    public static final DeferredItem<Item> GRAVITY_CHANGER_DOWN = ITEMS.register("gravity_changer_down", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.DOWN));
    public static final DeferredItem<Item> GRAVITY_CHANGER_UP = ITEMS.register("gravity_changer_up", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.UP));
    public static final DeferredItem<Item> GRAVITY_CHANGER_NORTH = ITEMS.register("gravity_changer_north", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.NORTH));
    public static final DeferredItem<Item> GRAVITY_CHANGER_SOUTH = ITEMS.register("gravity_changer_south", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.SOUTH));
    public static final DeferredItem<Item> GRAVITY_CHANGER_WEST = ITEMS.register("gravity_changer_west", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.WEST));
    public static final DeferredItem<Item> GRAVITY_CHANGER_EAST = ITEMS.register("gravity_changer_east", () -> new GravityChangerItem(new Properties().stacksTo(1), Direction.EAST));

    public static final DeferredItem<Item> GRAVITY_PLATING = ITEMS.register("plating", () -> new GravityPlatingItem(GravityBlocks.GRAVITY_PLATING.get(), new Properties()));

    public static final DeferredItem<Item> GRAVITY_CHANGER_DOWN_AOE = ITEMS.register("gravity_changer_down_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.DOWN));
    public static final DeferredItem<Item> GRAVITY_CHANGER_UP_AOE = ITEMS.register("gravity_changer_up_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.UP));
    public static final DeferredItem<Item> GRAVITY_CHANGER_NORTH_AOE = ITEMS.register("gravity_changer_north_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.NORTH));
    public static final DeferredItem<Item> GRAVITY_CHANGER_SOUTH_AOE = ITEMS.register("gravity_changer_south_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.SOUTH));
    public static final DeferredItem<Item> GRAVITY_CHANGER_WEST_AOE = ITEMS.register("gravity_changer_west_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.WEST));
    public static final DeferredItem<Item> GRAVITY_CHANGER_EAST_AOE = ITEMS.register("gravity_changer_east_aoe", () -> new GravityChangerItemAOE(new Properties().stacksTo(1), Direction.EAST));

    public static final DeferredItem<Item> GRAVITY_ANCHOR_DOWN = ITEMS.register("gravity_anchor_down", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.DOWN));
    public static final DeferredItem<Item> GRAVITY_ANCHOR_UP = ITEMS.register("gravity_anchor_up", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.UP));
    public static final DeferredItem<Item> GRAVITY_ANCHOR_NORTH = ITEMS.register("gravity_anchor_north", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.NORTH));
    public static final DeferredItem<Item> GRAVITY_ANCHOR_SOUTH = ITEMS.register("gravity_anchor_south", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.SOUTH));
    public static final DeferredItem<Item> GRAVITY_ANCHOR_WEST = ITEMS.register("gravity_anchor_west", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.WEST));
    public static final DeferredItem<Item> GRAVITY_ANCHOR_EAST = ITEMS.register("gravity_anchor_east", () -> new GravityAnchorItem(new Properties().stacksTo(1), Direction.EAST));
}
