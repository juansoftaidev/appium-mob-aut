package com.autjuan.manager;

import static io.github.the_sdet.common.CommonUtils.waitFor;
import static utils.ConfigReader.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.extern.java.Log;

public class AppiumDriverManager {
	private static final ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
	private static AppiumDriverLocalService service;
	private static final String platform = getProperties().getProperty("platform").trim().toLowerCase();
	private static final String executionType = getProperties().getProperty("execution.type").toLowerCase();
	private static final String appPackage = isAndroid() ? getAndroidProperties().getProperty("app.package")
			: getIosProperties().getProperty("app.package");

	public static boolean isAndroid() {
		return platform.equals("android");
	}

	public static boolean isLocal() {
		return executionType.equals("local");
	}

	public static URL startAppiumServer() {
		AppiumServiceBuilder builder = new AppiumServiceBuilder()
				.withIPAddress(getProperties().getProperty("appium.server.url.local")).usingAnyFreePort();

		service = AppiumDriverLocalService.buildService(builder);
		service.start();
		service.clearOutPutStreams();
		URL appiumServerUrl = service.getUrl();
		Log.info("Appium Server started at: " + appiumServerUrl);
		return appiumServerUrl;
	}

	public static void stopAppiumServer() {
		if (service != null && service.isRunning()) {
			service.stop();
			Log.info("Appium server stopped.");
		}
	}

	public static void initializeDriver() {
		Properties properties = getProperties();
		AppiumDriver driver;
		URL appiumServerURL = isLocal() ? startAppiumServer()
				: frameUrl(properties.getProperty("appium.server.url.remote"));

		if (isAndroid()) {
			driver = isLocal() ? new AndroidDriver(appiumServerURL, getAndroidDesiredCapabilities())
					: new AndroidDriver(appiumServerURL, getAndroidRemoteDesiredCapabilities());
		} else {
			driver = isLocal() ? new IOSDriver(appiumServerURL, getIosDesiredCapabilities())
					: new IOSDriver(appiumServerURL, getIosRemoteDesiredCapabilities());
		}
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Long.parseLong(properties.getProperty("implicit.wait"))));
		Log.info("Driver Started....");
		tlDriver.set(driver);
	}

	public static AppiumDriver getDriver() {
		return tlDriver.get();
	}

	public static void quitDriver() {
		AppiumDriver driver = tlDriver.get();
		if (driver != null) {
			try {
				if (isAndroid()) {
					((AndroidDriver) driver).terminateApp(appPackage);
				} else {
					((IOSDriver) driver).terminateApp(appPackage);
				}
			} catch (WebDriverException e) {
				waitFor(Duration.ofSeconds(2));
				if (isAndroid()) {
					((AndroidDriver) driver).terminateApp(appPackage);
				}
			}
			waitFor(Duration.ofSeconds(2));
			driver.quit();
			Log.info("App Terminated...");
		}
	}

	public static void activateApp() {
		if (isAndroid()) {
			((AndroidDriver) getDriver()).activateApp(appPackage);
		} else {
			((IOSDriver) getDriver()).activateApp(appPackage);
		}
		waitFor(Duration.ofSeconds(1));
		Log.info("App Activated...");
	}

	public static void relaunchApp() {
		if (isAndroid()) {
			try {
				((AndroidDriver) getDriver()).terminateApp(appPackage);
			} catch (Exception e) {
				waitFor(Duration.ofSeconds(1));
				((AndroidDriver) getDriver()).terminateApp(appPackage);
			}
			waitFor(Duration.ofSeconds(1));
			((AndroidDriver) getDriver()).activateApp(appPackage);
		} else {
			((IOSDriver) getDriver()).terminateApp(appPackage);
			waitFor(Duration.ofSeconds(1));
			((IOSDriver) getDriver()).activateApp(appPackage);
		}
		waitFor(Duration.ofSeconds(1));
	}

	private static URL frameUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			Log.error("Incorrect URL... Please check and try again...", e);
			return null;
		}
	}

	private static DesiredCapabilities getAndroidDesiredCapabilities() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		Properties androidProperties = getAndroidProperties();
		capabilities.setCapability("deviceName", androidProperties.getProperty("device.name"));
		capabilities.setCapability("platformName", getProperties().getProperty("platform"));
		capabilities.setCapability("platformVersion", androidProperties.getProperty("platform.version"));
		capabilities.setCapability("automationName", "uiAutomator2");
		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity", androidProperties.getProperty("app.activity"));
		capabilities.setCapability("app", androidProperties.getProperty("app"));
		capabilities.setCapability("noReset", Boolean.parseBoolean(androidProperties.getProperty("no.reset")));
		capabilities.setCapability("unicodeKeyboard",
				Boolean.parseBoolean(androidProperties.getProperty("unicode.keyboard")));
		capabilities.setCapability("resetKeyboard",
				Boolean.parseBoolean(androidProperties.getProperty("hide.keyboard")));
		capabilities.setCapability("sessionCreationRetry", androidProperties.getProperty("session.creation.retry"));
		capabilities.setCapability("sessionCreationTimeout", androidProperties.getProperty("session.creation.timeout"));
		capabilities.setCapability("appWaitDuration", androidProperties.getProperty("app.wait.timeout"));
		capabilities.setCapability("newCommandTimeout", androidProperties.getProperty("new.command.timeout"));
		capabilities.setCapability("autoGrantPermissions",
				Boolean.parseBoolean(androidProperties.getProperty("auto.grant.permissions")));
		capabilities.setCapability("autoAcceptAlerts",
				Boolean.parseBoolean(androidProperties.getProperty("auto.accept.alerts")));
		capabilities.setCapability("locationServicesAuthorized",
				Boolean.parseBoolean(androidProperties.getProperty("location.services")));
		capabilities.setCapability("disableWindowAnimation",
				Boolean.parseBoolean(androidProperties.getProperty("disable.window.animation")));
		capabilities.setCapability("disableAndroidWatchers",
				Boolean.parseBoolean(androidProperties.getProperty("disable.android.watchers")));
		capabilities.setCapability("ignoreUnimportantViews",
				Boolean.parseBoolean(androidProperties.getProperty("ignore.unimportant.views")));
		capabilities.setCapability("disableNotifications",
				Boolean.parseBoolean(androidProperties.getProperty("disable.notifications")));
		return capabilities;
	}

	private static DesiredCapabilities getIosDesiredCapabilities() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		Properties iosProperties = getIosProperties();
		capabilities.setCapability("deviceName", iosProperties.getProperty("device.name"));
		capabilities.setCapability("platformName", getProperties().getProperty("platform"));
		capabilities.setCapability("platformVersion", iosProperties.getProperty("platform.version"));
		capabilities.setCapability("automationName", "xcuitest");
		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity", iosProperties.getProperty("app.activity"));
		capabilities.setCapability("app", iosProperties.getProperty("app"));
		capabilities.setCapability("noReset", Boolean.parseBoolean(iosProperties.getProperty("no.reset")));
		capabilities.setCapability("unicodeKeyboard",
				Boolean.parseBoolean(iosProperties.getProperty("unicode.keyboard")));
		capabilities.setCapability("resetKeyboard", Boolean.parseBoolean(iosProperties.getProperty("hide.keyboard")));
		capabilities.setCapability("sessionCreationRetry", iosProperties.getProperty("session.creation.retry"));
		capabilities.setCapability("sessionCreationTimeout", iosProperties.getProperty("session.creation.timeout"));
		capabilities.setCapability("appWaitDuration", iosProperties.getProperty("app.wait.timeout"));
		capabilities.setCapability("newCommandTimeout", iosProperties.getProperty("new.command.timeout"));
		capabilities.setCapability("autoGrantPermissions",
				Boolean.parseBoolean(iosProperties.getProperty("auto.grant.permissions")));
		capabilities.setCapability("autoAcceptAlerts",
				Boolean.parseBoolean(iosProperties.getProperty("auto.accept.alerts")));
		capabilities.setCapability("locationServicesAuthorized",
				Boolean.parseBoolean(iosProperties.getProperty("location.services")));
		capabilities.setCapability("disableWindowAnimation",
				Boolean.parseBoolean(iosProperties.getProperty("disable.window.animation")));
		capabilities.setCapability("disableAndroidWatchers",
				Boolean.parseBoolean(iosProperties.getProperty("disable.android.watchers")));
		capabilities.setCapability("ignoreUnimportantViews",
				Boolean.parseBoolean(iosProperties.getProperty("ignore.unimportant.views")));
		capabilities.setCapability("disableNotifications",
				Boolean.parseBoolean(iosProperties.getProperty("disable.notifications")));
		return capabilities;
	}

	private static MutableCapabilities getAndroidRemoteDesiredCapabilities() {
		Properties androidProperties = getAndroidProperties();
		MutableCapabilities caps = new MutableCapabilities();
		caps.setCapability("platformName", getProperties().getProperty("platform"));
		String appName = androidProperties.getProperty("app.name");
		String appValue = appName.contains("apk") ? "storage:filename=" + appName : "storage:" + appName;
		caps.setCapability("app", appValue);
		caps.setCapability("deviceName", androidProperties.getProperty("sauce.device.name"));
		caps.setCapability("platformVersion", androidProperties.getProperty("sauce.platform.version"));
		caps.setCapability("automationName", "uiAutomator2");
		caps.setCapability("unicodeKeyboard", Boolean.parseBoolean(androidProperties.getProperty("unicode.keyboard")));
		caps.setCapability("resetKeyboard", Boolean.parseBoolean(androidProperties.getProperty("hide.keyboard")));
		caps.setCapability("sessionCreationRetry", androidProperties.getProperty("session.creation.retry"));
		caps.setCapability("sessionCreationTimeout", androidProperties.getProperty("session.creation.timeout"));
		caps.setCapability("appWaitDuration", androidProperties.getProperty("app.wait.timeout"));
		caps.setCapability("newCommandTimeout", androidProperties.getProperty("new.command.timeout"));
		caps.setCapability("sauceLabsImageInjectionEnabled", true);
		caps.setCapability("--session-override", true);
		caps.setCapability("autoGrantPermissions",
				Boolean.parseBoolean(androidProperties.getProperty("auto.grant.permissions")));
		caps.setCapability("androidDeviceReadyTimeout", androidProperties.getProperty("device.ready.timeout"));
		caps.setCapability("avdLaunchTimeout", androidProperties.getProperty("avd.launch.timeout"));
		caps.setCapability("avdReadyTimeout", androidProperties.getProperty("avd.launch.timeout"));
		caps.setCapability("sauce:options", getSauceOptions());
		return caps;
	}

	private static MutableCapabilities getIosRemoteDesiredCapabilities() {
		Properties iosProperties = getIosProperties();
		MutableCapabilities caps = new MutableCapabilities();
		caps.setCapability("platformName", getProperties().getProperty("platform"));
		String appName = iosProperties.getProperty("app.name");
		String appValue = appName.contains("apk") ? "storage:filename=" + appName : "storage:" + appName;
		caps.setCapability("app", appValue);
		caps.setCapability("deviceName", iosProperties.getProperty("sauce.device.name"));
		caps.setCapability("platformVersion", iosProperties.getProperty("sauce.platform.version"));
		caps.setCapability("automationName", "xcuitest");
		caps.setCapability("unicodeKeyboard", Boolean.parseBoolean(iosProperties.getProperty("unicode.keyboard")));
		caps.setCapability("resetKeyboard", Boolean.parseBoolean(iosProperties.getProperty("hide.keyboard")));
		caps.setCapability("sessionCreationRetry", iosProperties.getProperty("session.creation.retry"));
		caps.setCapability("sessionCreationTimeout", iosProperties.getProperty("session.creation.timeout"));
		caps.setCapability("appWaitDuration", iosProperties.getProperty("app.wait.timeout"));
		caps.setCapability("newCommandTimeout", iosProperties.getProperty("new.command.timeout"));
		caps.setCapability("sauceLabsImageInjectionEnabled", true);
		caps.setCapability("--session-override", true);
		caps.setCapability("autoGrantPermissions",
				Boolean.parseBoolean(iosProperties.getProperty("auto.grant.permissions")));
		caps.setCapability("androidDeviceReadyTimeout", iosProperties.getProperty("device.ready.timeout"));
		caps.setCapability("avdLaunchTimeout", iosProperties.getProperty("avd.launch.timeout"));
		caps.setCapability("avdReadyTimeout", iosProperties.getProperty("avd.launch.timeout"));
		caps.setCapability("sauce:options", getSauceOptions());
		return caps;
	}

	private static MutableCapabilities getSauceOptions() {
		Properties properties = getProperties();
		MutableCapabilities sauceOptions = new MutableCapabilities();
		sauceOptions.setCapability("build",
				properties.getProperty("app.under.test") + " v" + properties.getProperty("app.version"));
		sauceOptions.setCapability("name", properties.getProperty("sauce.labs.test.name"));
		sauceOptions.setCapability("deviceOrientation", properties.getProperty("sauce.labs.device.orientation"));
		return sauceOptions;
	}
}