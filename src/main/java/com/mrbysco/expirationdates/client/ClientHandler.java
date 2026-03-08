package com.mrbysco.expirationdates.client;

import com.mrbysco.expirationdates.registry.ExpirationData;
import com.mrbysco.expirationdates.registry.ExpirationDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ClientHandler {

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.has(ExpirationDataComponents.EXPIRATION_DATA)) {
			ExpirationData data = stack.get(ExpirationDataComponents.EXPIRATION_DATA);
			assert data != null;

			long progress = data.progress();
			long total = data.total();
			int secondsLeft = (int) ((total - progress) / 20);

			String formattedTime = formatSeconds(secondsLeft);
			event.getToolTip().add(1,
					Component.translatable("expirationdates.bestBefore", formattedTime)
							.withStyle(ChatFormatting.YELLOW)
			);
		}
	}

	private static String formatSeconds(int totalSeconds) {
		if (totalSeconds <= 0) {
			return "0 seconds";
		}

		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		if (minutes == 0) {
			return seconds + (seconds == 1 ? " second" : " seconds");
		}

		if (seconds == 0) {
			return minutes + (minutes == 1 ? " minute" : " minutes");
		}

		String minutePart = minutes + (minutes == 1 ? " minute" : " minutes");
		String secondPart = seconds + (seconds == 1 ? " second" : " seconds");
		return minutePart + " and " + secondPart;
	}
}
