package com.mrbysco.expirationdates;

import com.mojang.logging.LogUtils;
import com.mrbysco.expirationdates.config.ExpirationConfig;
import com.mrbysco.expirationdates.registry.ExpirationDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(ExpirationDateMod.MOD_ID)
public class ExpirationDateMod {
	public static final String MOD_ID = "expirationdates";
	public static final Logger LOGGER = LogUtils.getLogger();

	public ExpirationDateMod(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, ExpirationConfig.commonSpec);

		ExpirationDataComponents.DATA_COMPONENT_TYPES.register(eventBus);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
