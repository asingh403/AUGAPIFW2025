package com.qa.api.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManger {

	private static Properties properties = new Properties();

	static {
		try {
			InputStream input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config/config.properties");
			if (input == null) {
				input = ConfigManger.class.getClassLoader().getResourceAsStream("config/config.properties");
			}

			if (input != null) {
				properties.load(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		String value = properties.getProperty(key);
		return value != null ? value.trim() : null;
	}

	public static void set(String key, String value) {
		properties.setProperty(key, value);
	}

}