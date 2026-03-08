package com.mrbysco.expirationdates.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.mrbysco.expirationdates.ExpirationDateMod;
import com.mrbysco.expirationdates.resources.ExpirationDate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class ExpirationProvider implements DataProvider {
	private final PackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final String modid;
	private final Map<String, WithConditions<ExpirationDate>> toSerialize = new HashMap<>();


	public ExpirationProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modid) {
		this.output = packOutput;
		this.registries = registries;
		this.modid = modid;
	}

	@Override
	public final CompletableFuture<?> run(CachedOutput cache) {
		return this.registries.thenCompose(registries -> this.run(cache, registries));
	}

	public CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		start();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		Path expirationDateFolder = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(ExpirationDateMod.MOD_ID).resolve("expiration_date");
		for (var entry : toSerialize.entrySet()) {
			var name = entry.getKey();
			var modifier = entry.getValue();
			Path modifierPath = expirationDateFolder.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, registries, ExpirationDate.CONDITIONAL_CODEC, Optional.of(modifier), modifierPath));
		}

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	protected abstract void start();

	public void addExpirationDate(String id, ExpirationDate instance, List<ICondition> conditions) {
		this.toSerialize.put(id, new WithConditions<>(conditions, instance));
	}

	public void addExpirationDate(String id, ExpirationDate instance, ICondition... conditions) {
		addExpirationDate(id, instance, Arrays.asList(conditions));
	}

	@Override
	public String getName() {
		return "Expiration Dates: " + modid;
	}
}
