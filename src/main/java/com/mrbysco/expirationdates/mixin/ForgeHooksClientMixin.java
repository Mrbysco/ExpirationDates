package com.mrbysco.expirationdates.mixin;

import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
	@Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Z",
			cancellable = true, remap = false)
	private static void expirationdate$ShouldCauseReequipAnimation(ItemStack from, ItemStack to, int slot,
	                                                               CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (from.hasTag() && from.getTag() != null && to.hasTag() && to.getTag() != null
				&& from.getTag().getInt(ExpirationDateMod.EXPIRATION_PROGRESS) != to.getTag().getInt(ExpirationDateMod.EXPIRATION_PROGRESS)) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
}