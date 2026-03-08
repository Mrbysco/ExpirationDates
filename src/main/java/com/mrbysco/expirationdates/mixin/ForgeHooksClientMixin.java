package com.mrbysco.expirationdates.mixin;

import com.mrbysco.expirationdates.registry.ExpirationDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientHooks.class)
public class ForgeHooksClientMixin {
	@Inject(at = @At("RETURN"), method = "shouldCauseReequipAnimation(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Z",
			cancellable = true, remap = false)
	private static void expirationdate$ShouldCauseReequipAnimation(ItemStack from, ItemStack to, int slot,
	                                                               CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if (from.has(ExpirationDataComponents.EXPIRATION_DATA) && from.has(ExpirationDataComponents.EXPIRATION_DATA) &&
				from.get(ExpirationDataComponents.EXPIRATION_DATA) != to.get(ExpirationDataComponents.EXPIRATION_DATA)) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
}