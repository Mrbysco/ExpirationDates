package com.mrbysco.expirationdates;

import com.mojang.logging.LogUtils;
import com.mrbysco.expirationdates.config.ConfigHandler;
import com.mrbysco.expirationdates.config.ConfigReloadManager;
import com.mrbysco.expirationdates.config.ExpirationConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ExpirationDateMod.MOD_ID)
public class ExpirationDateMod {
	public static final String MOD_ID = "expirationdates";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static String EXPIRATION_TAG = MOD_ID + ":ExpirationDate";
	public static String EXPIRATION_PROGRESS = MOD_ID + ":ExpirationProgress";
	public static String EXPIRATION_TOTAL = MOD_ID + ":ExpirationTotal";

	public ExpirationDateMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExpirationConfig.commonSpec);

		eventBus.addListener(this::loadComplete);

		MinecraftForge.EVENT_BUS.register(new ConfigReloadManager());
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		ConfigHandler.initializeConfig();
	}
}
