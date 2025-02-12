package com.autjuan.engine;


import static utils.ConfigReader.*;

import java.io.File; // Import File for S3 upload example

import com.autjuan.confiig.AWSConfig;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class AppiumTestManager { // Renamed class
    static ThreadLocal<AppiumDriver> tlDriver = new ThreadLocal<>();
    private static AppiumDriverLocalService service;
    private static final String platform = getProperties().getProperty("platform").trim().toLowerCase();
    private static final String executionType = getProperties().getProperty("execution.type").toLowerCase();
    private static final String appPackage = isAndroid() ?
            getAndroidProperties().getProperty("app.package") : getIosProperties().getProperty("app.package");
    
    private AWSConfig awsConfig; // AWS Configuration

    public AppiumTestManager() { // Updated constructor
        this.awsConfig = new AWSConfig(); 
    }

    // Existing methods...

    public void uploadFileToS3(String bucketName, String filePath) {
        // Example method to upload a file to S3
        try {
            awsConfig.getS3Client().putObject(bucketName, new File(filePath).getName(), new File(filePath));
            Log.info("File uploaded to S3: " + filePath);
        } catch (Exception e) {
            Log.error("Failed to upload file to S3", e);
        }
    }

    // Other existing methods...
}