package com.mrbysco.expirationdates.datagen;

import com.mrbysco.expirationdates.ExpirationDateMod;
import com.mrbysco.expirationdates.datagen.provider.ExpirationProvider;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ExpirationDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

		if (event.includeClient()) {
			generator.addProvider(true, new ExpirationLanguageProvider(packOutput));
		}

		if (event.includeServer()) {
			generator.addProvider(true, new Expirations(packOutput, registries));
		}
	}

	public static class ExpirationLanguageProvider extends LanguageProvider {
		public ExpirationLanguageProvider(PackOutput output) {
			super(output, ExpirationDateMod.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("expirationdates.bestBefore", "Best Before: %s");
			add("expirationdates.expired", "%s has expired!");
		}
	}

	public static class Expirations extends ExpirationProvider {

		public Expirations(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
			super(packOutput, registries, ExpirationDateMod.MOD_ID);
		}

		@Override
		protected void start() {
			addExpirationDate("potions", new ExpirationDate(List.of(
					Ingredient.of(Items.POTION, Items.LINGERING_POTION, Items.SPLASH_POTION)
			), 36000L, new ItemStack(Items.GLASS_BOTTLE)));

			addExpirationDate("stews", new ExpirationDate(List.of(
					Ingredient.of(Items.SUSPICIOUS_STEW, Items.MUSHROOM_STEW,
							Items.RABBIT_STEW, Items.BEETROOT_SOUP)
			), 36000L, new ItemStack(Items.BOWL)));
		}
	}
}
