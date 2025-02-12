package com.autjuan.confiig;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

public class VPNSetup {
    public static void main(String[] args) {
        String accessKey = System.getenv("25e9ccaaff2a78bb6be7407d1ca09f07cd36e63a");
        String secretKey = System.getenv("407d1ca09f07cd36e63a");
        String region = System.getenv("us-east-1");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();

        // Add your logic to create and configure the OpenVPN instance
    }
}