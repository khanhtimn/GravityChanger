package dev.khanhtimn.gravitychanger.data;

import dev.khanhtimn.gravitychanger.GravityChanger;
import dev.khanhtimn.gravitychanger.data.attachments.GravityData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class GravityAttachedData {

    public static final DeferredRegister<AttachmentType<?>> DATA_ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GravityChanger.MODID);

    public static final Supplier<AttachmentType<GravityData>> GRAVITY_DATA = DATA_ATTACHMENT_TYPES.register(
            "gravity",
            () -> AttachmentType.builder(GravityData::new)
                    .serialize(GravityData.CODEC)
                    .copyOnDeath()
                    .build()
    );
}
