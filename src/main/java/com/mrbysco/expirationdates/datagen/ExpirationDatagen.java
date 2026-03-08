package com.mrbysco.expirationdates.datagen;

import com.mrbysco.expirationdates.ExpirationDateMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExpirationDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeClient()) {
			generator.addProvider(true, new ExpirationLanguageProvider(packOutput));
		}
	}

	public static class ExpirationLanguageProvider extends LanguageProvider {
		public ExpirationLanguageProvider(PackOutput output) {
			super(output, ExpirationDateMod.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("expirationdates.bestBefore", "Best Before: %s");
			add("expirationdates.expired", "%s has expired!");
		}
	}
}
