package com.szw.payment.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szw.payment.common.annotation.ExtraKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtraKeysUtil {


	public static Map<String, String> findExtraKeys(Object target) {
		Map<String /* key name */, String /* field value */> map = new HashMap<>();
		if (target == null) {
			return map;
		}

		List<Field> allFields = new ArrayList<>();
		for (Class<?> cls = target.getClass(); cls != null && cls != Object.class; ) {
			Field[] fields = cls.getDeclaredFields();
			allFields.addAll(List.of(fields));
			cls = cls.getSuperclass();
		}

		for (Field f : allFields) {
			ExtraKey ann = f.getAnnotation(ExtraKey.class);
			if (ann == null) {
				continue;
			}
			try {
				f.setAccessible(true);
				Object value = f.get(target);
				if (value != null) {
					map.putIfAbsent(ann.value(), value.toString());
				}
			}
			catch (IllegalAccessException e) {
				// ignore
			}
		}
		return map;
	}

}
