package com.mrbysco.expirationdates.client;

import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientHandler {

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.hasTag() && stack.getTag().contains(ExpirationDateMod.EXPIRATION_TAG)) {
			long progress = stack.getTag().getLong(ExpirationDateMod.EXPIRATION_PROGRESS);
			long total = stack.getTag().getLong(ExpirationDateMod.EXPIRATION_TOTAL);
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
