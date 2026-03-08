package com.mrbysco.expirationdates.mixin;

import com.mrbysco.expirationdates.util.BarHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

	@Inject(
			at = @At("HEAD"),
			method = "isBarVisible(Lnet/minecraft/world/item/ItemStack;)Z",
			cancellable = true
	)
	public void expirationdate$isBarVisible(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (BarHelper.isBarVisible(stack)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(
			at = @At("HEAD"),
			method = "getBarColor(Lnet/minecraft/world/item/ItemStack;)I",
			cancellable = true
	)
	public void expirationdate$getBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (BarHelper.isBarVisible(stack)) {
			int color = BarHelper.getBarColor(stack);
			if (color != -1) {
				cir.setReturnValue(color);
			}
		}
	}

	@Inject(
			at = @At("HEAD"),
			method = "getBarWidth(Lnet/minecraft/world/item/ItemStack;)I",
			cancellable = true
	)
	public void expirationdate$getBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (BarHelper.isBarVisible(stack)) {
			int width = BarHelper.getBarWidth(stack);
			if (width != -1) {
				cir.setReturnValue(width);
			}
		}
	}
}
