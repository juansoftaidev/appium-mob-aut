package com.autjuan.confiig;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AWSConfig {
	private AmazonS3 s3Client;

	public AWSConfig() {
		 String accessKey = System.getenv("25e9ccaaff2a78bb6be7407d1ca09f07cd36e63a");
	        String secretKey = System.getenv("407d1ca09f07cd36e63a");
	        String region = System.getenv("us-east-1");

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		this.s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(region).build();
	}

	public AmazonS3 getS3Client() {
		return s3Client;
	}

	// Add other AWS services as needed
}