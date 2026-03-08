package com.mrbysco.expirationdates.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.lang.reflect.Type;
import java.util.List;

public class ExpirationDate {
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

	public static class DateSerializer implements JsonSerializer<ExpirationDate>, JsonDeserializer<ExpirationDate> {

		@Override
		public JsonElement serialize(ExpirationDate expirationDate, Type type, JsonSerializationContext serializationContext) {
			JsonObject json = new JsonObject();

			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : expirationDate.items) {
				jsonarray.add(ingredient.toJson());
			}

			json.add("items", jsonarray);
			json.addProperty("expirationTime", expirationDate.expirationTime);

			if (!expirationDate.result.isEmpty()) {
				ItemStack result = expirationDate.result;
				JsonObject itemObject = new JsonObject();
				itemObject.addProperty("item", BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
				if (result.getCount() > 1) {
					itemObject.addProperty("count", result.getCount());
				}

				json.add("result", itemObject);
			}

			return json;
		}

		@Override
		public ExpirationDate deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			final JsonObject json = jsonElement.getAsJsonObject();

			NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "items"));
			long expirationTime = GsonHelper.getAsLong(json, "expirationTime");

			ItemStack result = ItemStack.EMPTY;
			if (json.has("result")) {
				result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			}

			return new ExpirationDate(nonnulllist, expirationTime, result);
		}

		private static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i), false);
				if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}
	}
}
