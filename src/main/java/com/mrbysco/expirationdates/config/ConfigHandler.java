package com.mrbysco.expirationdates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbysco.expirationdates.ExpirationDateMod;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(ExpirationDate.class, new ExpirationDate.DateSerializer()).create();

	public static final File DATE_FOLDER = new File(FMLPaths.CONFIGDIR.get().toFile() + "/expirationdates");

	public static final List<ExpirationDate> EXPIRATION_DATES = new ArrayList<>();

	public static void initializeConfig() {
		if (!DATE_FOLDER.exists()) {
			DATE_FOLDER.mkdirs();

			createDefaultFile("potions.json", new ExpirationDate(List.of(
					Ingredient.of(Items.POTION, Items.LINGERING_POTION, Items.SPLASH_POTION)
			), 36000L, new ItemStack(Items.GLASS_BOTTLE)));

			createDefaultFile("stews.json", new ExpirationDate(List.of(
					Ingredient.of(Items.SUSPICIOUS_STEW, Items.MUSHROOM_STEW,
							Items.RABBIT_STEW, Items.BEETROOT_SOUP)
			), 36000L, new ItemStack(Items.BOWL)));
		}

		// Load all JSON files from the folder
		loadAllConfigs();
	}

	private static void createDefaultFile(String fileName, ExpirationDate expirationDate) {
		File file = new File(DATE_FOLDER, fileName);
		if (!file.exists()) {
			try (FileWriter writer = new FileWriter(file)) {
				GSON.toJson(expirationDate, writer);
				writer.flush();
			} catch (IOException e) {
				ExpirationDateMod.LOGGER.error("Failed to create default file: {}", fileName, e);
			}
		}
	}

	public static void loadAllConfigs() {
		EXPIRATION_DATES.clear();

		File[] files = DATE_FOLDER.listFiles((dir, name) -> name.endsWith(".json"));
		if (files == null || files.length == 0) {
			ExpirationDateMod.LOGGER.warn("No expiration date configuration files found!");
			return;
		}

		for (File file : files) {
			loadConfig(file);
		}

		ExpirationDateMod.LOGGER.info("Loaded {} expiration date configuration(s)", EXPIRATION_DATES.size());
	}

	private static void loadConfig(File file) {
		String fileName = file.getName();
		try (FileReader json = new FileReader(file)) {
			final ExpirationDate expirationDate = GSON.fromJson(json, ExpirationDate.class);
			if (expirationDate != null) {
				EXPIRATION_DATES.add(expirationDate);
				ExpirationDateMod.LOGGER.info("Loaded expiration dates from: {}", fileName);
			} else {
				ExpirationDateMod.LOGGER.error("Could not load expiration dates from {}.", fileName);
			}
		} catch (final Exception e) {
			ExpirationDateMod.LOGGER.error("Unable to load file {}. Please make sure it's a valid json.", fileName, e);
		}
	}

	/**
	 * Checks all loaded expiration date configs for a matching item and returns the first one found. If no match is found, returns null.
	 *
	 * @param stack The item stack to check for expiration data
	 * @return The expiration date data for the given item stack, or null if no matching data is found
	 */
	public static ExpirationDate getExpirationDataForItem(ItemStack stack) {
		for (ExpirationDate expirationDate : EXPIRATION_DATES) {
			for (Ingredient ingredient : expirationDate.items()) {
				if (ingredient.test(stack)) {
					return expirationDate;
				}
			}
		}
		return null;
	}
}
