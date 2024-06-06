package com.szw.payment.sdk;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.szw.payment.common.model.ConfigInfo;
import com.szw.payment.sdk.exception.PayException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PayBuilder {
	private static final Map<String, Class<?>> CLS_MAP = new ConcurrentHashMap<>();

	static {
		String filePath = "mapping.json";
		ClassLoader loader = PayBuilder.class.getClassLoader();
		try (InputStream inputStream = loader.getResourceAsStream(filePath)) {
			assert inputStream != null;

			Type type = new TypeToken<List<Mapping>>() {}.getType();
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

			List<Mapping> list = gson.fromJson(reader, type);
			list.forEach(mapping -> {
				Class<?> aClass = loadClass(mapping.getCls());
				if (aClass != null) {
					String key = createKey(mapping.getChannel(), mapping.getApiVersion());
					CLS_MAP.put(key, aClass);
				}
			});
		}
		catch (Exception e) {
			// ignore
		}
	}

	private ConfigInfo configInfo;

	private PayExceptionHandler exceptionHandler;


	public static PayBuilder create() {
		return new PayBuilder();
	}

	public PayBuilder config(ConfigInfo configInfo) {
		this.configInfo = configInfo;
		return this;
	}

	public PayBuilder exceptionHandler(PayExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	public Pay build() {
		try {
			String key = createKey(configInfo.getChannel(), configInfo.getApiVersion());
			Class<?> cls = CLS_MAP.get(key);
			Object target = cls.getDeclaredConstructor().newInstance();
			Pay pay = (Pay) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(),
					new PayInvocationHandler(target, exceptionHandler));

			pay.initConfig(configInfo);
			return pay;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private record PayInvocationHandler(Object target,
	                                    PayExceptionHandler handler) implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				return method.invoke(target, args);
			}
			catch (InvocationTargetException e) {
				if (e.getCause() instanceof PayException exception) {
					if (handler == null || handler.onException(method, exception)) {
						throw exception;
					}
					return null;
				}
				throw e.getCause();
			}
		}
	}

	@Setter
	@Getter
	private static class Mapping {

		private String channel;

		private String apiVersion;

		private String cls;

	}

	private static Class<?> loadClass(String clsName) {
		try {
			return Class.forName(clsName);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
	}

	private static String createKey(String channel, String apiVersion) {
		return channel + "@" + apiVersion;
	}

}
