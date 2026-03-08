package com.mrbysco.expirationdates.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.List;
import java.util.Optional;

public class ExpirationDate {
	public static final ResourceKey<Registry<ExpirationDate>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			ExpirationDateMod.modLoc("expiration_date"));

	public static final Codec<ExpirationDate> DIRECT_CODEC = ExtraCodecs.catchDecoderException(
			RecordCodecBuilder.create(
					instance -> instance.group(
							Ingredient.CODEC.listOf().fieldOf("items").forGetter(ExpirationDate::items),
							Codec.LONG.fieldOf("expirationTime").forGetter(ExpirationDate::expirationTime),
							ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(ExpirationDate::result)
					).apply(instance, ExpirationDate::new)
			)
	);

	public static final Codec<Optional<WithConditions<ExpirationDate>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final List<Ingredient> items;
	protected final long expirationTime;
	protected final ItemStack result;

	public ExpirationDate(List<Ingredient> items, long expirationTime, ItemStack result) {
		this.items = items;
		this.expirationTime = expirationTime;
		this.result = result;
	}

	public ExpirationDate(List<Ingredient> items, long expirationTime) {
		this(items, expirationTime, ItemStack.EMPTY);
	}

	public List<Ingredient> items() {
		return items;
	}

	public long expirationTime() {
		return expirationTime;
	}

	public ItemStack result() {
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExpirationDate that = (ExpirationDate) o;
		return expirationTime == that.expirationTime && items.equals(that.items) && ItemStack.isSameItem(result, that.result);
	}

	@Override
	public int hashCode() {
		int result = items.hashCode();
		result = 31 * result + Long.hashCode(expirationTime);
		return result;
	}
}
