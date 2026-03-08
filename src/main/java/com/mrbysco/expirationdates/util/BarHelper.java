package com.mrbysco.expirationdates.util;

import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class BarHelper {

	public static boolean isBarVisible(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains(ExpirationDateMod.EXPIRATION_TAG)) {
			return true;
		}
		return false;
	}

	public static int getBarWidth(ItemStack stack) {
		if (isBarVisible(stack)) {
			CompoundTag tag = stack.getTag();
			assert tag != null;
			int progress = tag.getInt(ExpirationDateMod.EXPIRATION_PROGRESS);
			int total = tag.getInt(ExpirationDateMod.EXPIRATION_TOTAL);
			if (total == 0) return -1;
			return Math.round(13.0F - (float) progress * 13.0F / (float) total);
		}
		return -1;
	}

	public static int getBarColor(ItemStack stack) {
		if (isBarVisible(stack)) {
			CompoundTag tag = stack.getTag();
			assert tag != null;
			int progress = tag.getInt(ExpirationDateMod.EXPIRATION_PROGRESS);
			int total = tag.getInt(ExpirationDateMod.EXPIRATION_TOTAL);
			float f = Math.max(0.0F, (total - (float) progress) / total);
			if (total == 0) return -1;
			return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
		}
		return -1;
	}

}
