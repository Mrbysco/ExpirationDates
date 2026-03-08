package com.mrbysco.expirationdates.handler;

import com.mrbysco.expirationdates.config.ExpirationConfig;
import com.mrbysco.expirationdates.registry.ExpirationData;
import com.mrbysco.expirationdates.registry.ExpirationDataComponents;
import com.mrbysco.expirationdates.registry.ExpirationRegistry;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ExpirationHandler {

	@SubscribeEvent
	public static void onItemTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide &&
				level.getGameTime() % 20 == 0 && !player.getAbilities().instabuild) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stack.isEmpty()) continue;

				ExpirationDate expirationData = ExpirationRegistry.getExpirationDataForItem(stack);
				if (expirationData == null) continue;

				ExpirationData expiration = stack.get(ExpirationDataComponents.EXPIRATION_DATA);
				if (expiration == null) {
					// Initialize expiration data
					stack.set(ExpirationDataComponents.EXPIRATION_DATA, new ExpirationData(
							level.getGameTime() + expirationData.expirationTime(),
							0L,
							expirationData.expirationTime()
					));
				} else {
					// Update expiration progress
					long expirationTime = expiration.expirationDate();
					long totalTime = expiration.total();

					if (level.getGameTime() >= expirationTime) {
						if (ExpirationConfig.COMMON.announceInChat.get()) {
							player.displayClientMessage(
									Component.translatable("expirationdates.expired", stack.getHoverName()).withStyle(ChatFormatting.RED),
									false
							);
						}
						ItemStack result = expirationData.result().copy();
						inventory.removeItem(i, 1);
						if (!result.isEmpty()) {
							if (!inventory.add(result)) {
								player.drop(result, false);
							}
						}
					} else {
						int progress = (int) (expirationTime - level.getGameTime());
						stack.set(ExpirationDataComponents.EXPIRATION_DATA, new ExpirationData(
								expirationTime,
								totalTime - progress,
								totalTime
						));
					}
				}
			}
		}
	}
}
