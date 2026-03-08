package com.mrbysco.expirationdates.config;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConfigReloadManager implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		ConfigHandler.loadAllConfigs();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(this);
	}
}
