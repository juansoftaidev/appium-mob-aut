package com.autjuan.utils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

/**
 * Utility class to manage the Appium server and driver for Android automation.
 */
public class AppiumServer {

	private static final String NODE_PATH = "";
	private static final String APPIUM_JS_PATH = ""
			+ "";
	private static final int APPIUM_PORT = 4723;
	private static final long IMPLICIT_WAIT_TIME = 15;

	private AppiumDriverLocalService service;
	private DesiredCapabilities capabilities;
	private AndroidDriver<MobileElement> driver;
	private final File app;

	public AppiumServer() {
		File root = new File(System.getProperty("user.dir"));
		this.app = new File(root, "/src/test/api/qaapk.apk");
	}

	/**
	 * Starts the Appium server and initializes the Android driver.
	 *
	 * @return AndroidDriver instance
	 * @throws Exception if there is an error starting the server or driver
	 */
	public AndroidDriver<MobileElement> startServer() throws Exception {
		setupCapabilities();
		startAppiumService();
		initializeDriver();
		return driver;
	}

	/**
	 * Stops the Appium server.
	 */
	public void stopServer() {
		if (service != null && service.isRunning()) {
			service.stop();
		}
	}

	/**
	 * Checks if the Appium server is running on the specified port.
	 *
	 * @param port the port to check
	 * @return true if the server is running, false otherwise
	 */
	public boolean checkIfServerIsRunning(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			return false; // Port is free, server is not running
		} catch (IOException e) {
			return true; // Port is in use, server is running
		}
	}

	private void setupCapabilities() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "nexus5");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		capabilities.setCapability("appPackage", "com.aa.android.qa");
		capabilities.setCapability("appActivity", "com.aa.android.view.SplashActivity");
		capabilities.setCapability("noReset", "false");
	}

	private void startAppiumService() {
		AppiumServiceBuilder builder = new AppiumServiceBuilder().usingDriverExecutable(new File(NODE_PATH))
				.withAppiumJS(new File(APPIUM_JS_PATH)).withIPAddress("0.0.0.0").usingPort(APPIUM_PORT)
				.withCapabilities(capabilities).withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

		service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}

	private void initializeDriver() {
		driver = new AndroidDriver<>(service.getUrl(), capabilities);
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
	}
}