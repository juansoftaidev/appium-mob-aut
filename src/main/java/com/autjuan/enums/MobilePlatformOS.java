package com.autjuan.enums;

/**
 * Enum representing the supported mobile platforms and operating systems.
 */
public enum MobilePlatformOS {
	ANDROID("Android Platform"), IOS("iOS Platform");

	private final String description;

	MobilePlatformOS(String description) {
		this.description = description;
	}

	/**
	 * Gets the description of the mobile platform operating system.
	 *
	 * @return the description of the operating system
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return description;
	}
}