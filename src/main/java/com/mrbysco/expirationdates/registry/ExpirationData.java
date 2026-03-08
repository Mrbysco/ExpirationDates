package com.mrbysco.expirationdates.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ExpirationData(long expirationDate, long progress, long total) {
	public static final Codec<ExpirationData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.LONG.fieldOf("expirationDate").forGetter(ExpirationData::expirationDate),
					Codec.LONG.fieldOf("progress").forGetter(ExpirationData::progress),
					Codec.LONG.fieldOf("total").forGetter(ExpirationData::total))
			.apply(inst, ExpirationData::new));

	public static final StreamCodec<FriendlyByteBuf, ExpirationData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG,
			ExpirationData::expirationDate,
			ByteBufCodecs.VAR_LONG,
			ExpirationData::progress,
			ByteBufCodecs.VAR_LONG,
			ExpirationData::total,
			ExpirationData::new);
}
