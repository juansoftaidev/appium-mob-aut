package com.autjuan.utils;

import java.util.Properties;

import io.cucumber.java.Scenario;

public class ScreenshotHandler {

	public static void attachScreenshotBasedOnConfig(Scenario scenario) {
		Properties properties = ConfigReader.getProperties();
		String screenshotConfig = properties.getProperty("screenshot");

		switch (screenshotConfig) {
		case "only.fail":
			handleFailureScreenshot(scenario);
			break;
		case "only.pass":
			handleSuccessScreenshot(scenario);
			break;
		default:
			LoggerUtil.info("Taking Screenshot for scenario: " + scenario.getName());
			attachScreenshot(getDriver());
			break;
		}
	}

	private static void handleFailureScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			logFailureToReport(scenario.getName() + " Failed...");
			attachScreenshot(getDriver());
		}
	}

	private static void handleSuccessScreenshot(Scenario scenario) {
		if (!scenario.isFailed()) {
			logSuccessToReport(scenario.getName() + " Passed...");
			attachScreenshot(getDriver());
		}
	}
}