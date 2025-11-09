package dev.khanhtimn.gravitychanger.data;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.data.components.PlatingComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GravityComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, GravityChanger.MODID);

    public static final Supplier<DataComponentType<PlatingComponent>> PLATING_SIDE_DATA =
            DATA_COMPONENT_TYPES.register("plating_side_data",
                    () -> DataComponentType.<PlatingComponent>builder()
                            .persistent(PlatingComponent.CODEC)
                            .networkSynchronized(PlatingComponent.STREAM_CODEC)
                            .build()
            );
}

