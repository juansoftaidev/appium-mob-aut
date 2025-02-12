package com.autjuan.confiig;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AWSConfig {
	private AmazonS3 s3Client;

	public AWSConfig() {
		String accessKey = System.getenv("AWS_ACCESS_KEY");
		String secretKey = System.getenv("AWS_SECRET_KEY");
		String region = System.getenv("AWS_REGION");

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		this.s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(region).build();
	}

	public AmazonS3 getS3Client() {
		return s3Client;
	}

	// Add other AWS services as needed
}