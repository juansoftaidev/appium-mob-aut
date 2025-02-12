package com.autjuan.confiig;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.devicefarm.AWSDeviceFarm;
import com.amazonaws.services.devicefarm.AWSDeviceFarmClientBuilder;
import com.amazonaws.services.devicefarm.model.CreateProjectRequest;
import com.amazonaws.services.devicefarm.model.CreateProjectResult;
import com.amazonaws.services.devicefarm.model.CreateUploadRequest;
import com.amazonaws.services.devicefarm.model.CreateUploadResult;
import com.amazonaws.services.devicefarm.model.Upload;
import com.amazonaws.services.devicefarm.model.UploadStatus;

public class DeviceFarmConfig {
    private AWSDeviceFarm deviceFarm;
    private String projectName;

    public DeviceFarmConfig() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String region = System.getenv("AWS_REGION");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        deviceFarm = AWSDeviceFarmClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
        this.projectName = "YourProjectName"; // Set your project name here
    }

    public String createProject() {
        CreateProjectRequest request = new CreateProjectRequest().withName(projectName);
        CreateProjectResult result = deviceFarm.createProject(request);
        return result.getProject().getArn();
    }

    public Upload createUpload(String projectArn, String appFilePath) {
        CreateUploadRequest request = new CreateUploadRequest()
                .withProjectArn(projectArn)
                .withName("YourAppName.apk") // Change to your app name
                .withType("ANDROID_APP"); // Change to "IOS_APP" if needed

        CreateUploadResult result = deviceFarm.createUpload(request);
        return result.getUpload();
    }

    public boolean isUploadComplete(Upload upload) {
        return upload.getStatus().equals(UploadStatus.SUCCESS);
    }
}