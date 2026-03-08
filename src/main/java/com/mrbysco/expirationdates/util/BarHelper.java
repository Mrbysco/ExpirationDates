package com.mrbysco.expirationdates.util;

import com.mrbysco.expirationdates.registry.ExpirationData;
import com.mrbysco.expirationdates.registry.ExpirationDataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class BarHelper {

	public static boolean isBarVisible(ItemStack stack) {
		return stack.has(ExpirationDataComponents.EXPIRATION_DATA);
	}

	public static int getBarWidth(ItemStack stack) {
		if (isBarVisible(stack)) {
			ExpirationData data = stack.get(ExpirationDataComponents.EXPIRATION_DATA);
			assert data != null;
			long progress = data.progress();
			long total = data.total();
			if (total == 0) return -1;
			return Math.round(13.0F - (float) progress * 13.0F / (float) total);
		}
		return -1;
	}

	public static int getBarColor(ItemStack stack) {
		if (isBarVisible(stack)) {
			ExpirationData data = stack.get(ExpirationDataComponents.EXPIRATION_DATA);
			assert data != null;
			long progress = data.progress();
			long total = data.total();
			float f = Math.max(0.0F, (total - (float) progress) / total);
			if (total == 0) return -1;
			return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
		}
		return -1;
	}

}
