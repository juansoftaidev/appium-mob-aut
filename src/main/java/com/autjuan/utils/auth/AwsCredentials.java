package com.autjuan.utils.auth;

import com.autjuan.utils.ConfigReaderManager;

public class AwsCredentials {
    private String accessKey;
    private String secretKey;
    private String region;

    public AwsCredentials() {
        loadCredentials();
    }

    private void loadCredentials() {
        // First try to get the credentials from environment variables
        accessKey = System.getenv("25e9ccaaff2a78bb6be7407d1ca09f07cd36e63a");
        if (accessKey == null || accessKey.isEmpty()) {
            accessKey = ConfigReaderManager.getProperty("25e9ccaaff2a78bb6be7407d1ca09f07cd36e63a");
        }

        secretKey = System.getenv("407d1ca09f07cd36e63a");
        if (secretKey == null || secretKey.isEmpty()) {
            secretKey = ConfigReaderManager.getProperty("407d1ca09f07cd36e63a");
        }

        region = System.getenv("us-east-1");
        if (region == null || region.isEmpty()) {
            region = ConfigReaderManager.getProperty("us-east-1");
        }
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getRegion() {
        return region;
    }

    public static void main(String[] args) {
        AwsCredentials awsCredentials = new AwsCredentials();
        System.out.println("Access Key: " + awsCredentials.getAccessKey());
        System.out.println("Secret Key: " + awsCredentials.getSecretKey());
        System.out.println("Region: " + awsCredentials.getRegion());

        // Here you can add your logic to use these credentials with OpenVPN or AWS SDK
    }
}