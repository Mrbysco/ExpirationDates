package com.mrbysco.expirationdates.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ExpirationConfig {
	public static class Common {
		public final ForgeConfigSpec.BooleanValue announceInChat;

		Common(ForgeConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			announceInChat = builder
					.comment("If true, players will receive a chat message when an item expires.")
					.define("announceInChat", true);

			builder.pop();
		}
	}


	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
