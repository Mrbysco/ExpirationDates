package com.mrbysco.expirationdates.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ExpirationConfig {
	public static class Common {
		public final ModConfigSpec.BooleanValue announceInChat;

		Common(ModConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			announceInChat = builder
					.comment("If true, players will receive a chat message when an item expires.")
					.define("announceInChat", true);

			builder.pop();
		}
	}


	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
