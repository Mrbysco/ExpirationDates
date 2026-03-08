package com.mrbysco.expirationdates.registry;

import com.mrbysco.expirationdates.ExpirationDateMod;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ExpirationRegistry {
	private static final Map<ResourceLocation, ExpirationDate> expirationDates = new HashMap<>();

	@SubscribeEvent
	public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(ExpirationDate.REGISTRY_KEY,
				ExpirationDate.DIRECT_CODEC, ExpirationDate.DIRECT_CODEC);
	}

	@SubscribeEvent
	public static void onTagsUpdated(OnDatapackSyncEvent event) {
		final RegistryAccess registryAccess = event.getPlayerList().getServer().registryAccess();

		expirationDates.clear();
		final Registry<ExpirationDate> biomePlaceRegistry = registryAccess.registryOrThrow(ExpirationDate.REGISTRY_KEY);
		biomePlaceRegistry.entrySet().forEach((key) -> expirationDates.put(key.getKey().location(), key.getValue()));
		ExpirationDateMod.LOGGER.info("Loaded Expiration Dates: {} dates", expirationDates.size());
	}

	/**
	 * Gets the expiration date for the given item stack, if it exists.
	 *
	 * @param stack The item stack to check for an expiration date.
	 * @return The expiration date for the given item stack, or null if no expiration date exists for it.
	 */
	public static ExpirationDate getExpirationDataForItem(ItemStack stack) {
		return expirationDates.values().stream()
				.filter(date -> date.items().stream().anyMatch(item -> item.test(stack)))
				.findFirst()
				.orElse(null);
	}
}
