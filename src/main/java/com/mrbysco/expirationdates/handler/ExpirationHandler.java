package com.mrbysco.expirationdates.handler;

import com.mrbysco.expirationdates.config.ConfigHandler;
import com.mrbysco.expirationdates.config.ExpirationConfig;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mrbysco.expirationdates.ExpirationDateMod.EXPIRATION_PROGRESS;
import static com.mrbysco.expirationdates.ExpirationDateMod.EXPIRATION_TAG;
import static com.mrbysco.expirationdates.ExpirationDateMod.EXPIRATION_TOTAL;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExpirationHandler {

	@SubscribeEvent
	public static void onItemTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		Level level = player.level();
		if (event.phase == TickEvent.Phase.END && !level.isClientSide &&
				level.getGameTime() % 20 == 0 && !event.player.getAbilities().instabuild) {
			Inventory inventory = player.getInventory();
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stack.isEmpty()) continue;

				ExpirationDate expirationData = ConfigHandler.getExpirationDataForItem(stack);
				if (expirationData == null) continue;

				CompoundTag tag = stack.hasTag() ? stack.getTag() : new CompoundTag();
				assert tag != null;

				if (!tag.contains(EXPIRATION_TAG)) {
					// Initialize expiration data
					tag.putLong(EXPIRATION_TAG, level.getGameTime() + expirationData.expirationTime());
					tag.putLong(EXPIRATION_PROGRESS, 0L);
					tag.putLong(EXPIRATION_TOTAL, expirationData.expirationTime());
					stack.setTag(tag);
				} else {
					// Update expiration progress
					long expirationTime = tag.getLong(EXPIRATION_TAG);
					long totalTime = tag.getLong(EXPIRATION_TOTAL);

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
						tag.putLong(EXPIRATION_PROGRESS, totalTime - progress);
					}
				}
			}
		}
	}
}
