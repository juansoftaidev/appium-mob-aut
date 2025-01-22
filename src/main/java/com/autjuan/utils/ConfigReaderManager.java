package com.autjuan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;

public class ConfigReaderManager {

	@Getter
	private static final Properties properties = new Properties();
	@Getter
	private static final Properties androidProperties = new Properties();
	@Getter
	private static final Properties iosProperties = new Properties();

	static {
		loadProperties("config.properties", properties);
		loadProperties("android-config.properties", androidProperties);
		loadProperties("ios-config.properties", iosProperties);
	}

	private static void loadProperties(String fileName, Properties properties) {
		try (InputStream input = ConfigReaderManager.class.getClassLoader().getResourceAsStream(fileName)) {
			if (input == null) {
				throw new IOException("Unable to find " + fileName);
			}
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load properties from " + fileName, e);
		}
	}

	public static String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public static String getAndroidProperty(String propertyName) {
		return androidProperties.getProperty(propertyName);
	}

	public static String getIosProperty(String propertyName) {
		return iosProperties.getProperty(propertyName);
	}
}