package com.szw.payment.common.utils;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GsonUtil {

	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(Date.class, new DateAdapter())
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.create();


	static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

		@Override
		public JsonElement serialize(LocalDateTime time, Type type, JsonSerializationContext jsonSerializationContext) {
			if (time == null) {
				return null;
			}
			long epochMilli = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			return new JsonPrimitive(epochMilli);
		}

		@Override
		public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			if (jsonElement == null) {
				return null;
			}
			long epochMilli = jsonElement.getAsLong();
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
		}
	}

	static class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			return jsonElement != null ? new Date(jsonElement.getAsLong()) : null;
		}

		@Override
		public JsonElement serialize(Date time, Type type, JsonSerializationContext jsonSerializationContext) {
			return time != null ? new JsonPrimitive(time.getTime()) : null;
		}
	}

}
