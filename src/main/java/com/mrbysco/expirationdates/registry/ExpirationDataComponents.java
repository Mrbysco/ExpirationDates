package com.mrbysco.expirationdates.registry;

import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ExpirationDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExpirationDateMod.MOD_ID);

	public static final Supplier<DataComponentType<ExpirationData>> EXPIRATION_DATA = DATA_COMPONENT_TYPES.registerComponentType("expiration_data", builder ->
			builder
					.persistent(ExpirationData.CODEC)
					.networkSynchronized(ExpirationData.STREAM_CODEC)
	);

}
