package org.testcontainers.minio;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class MinioS3Client implements AutoCloseable {

    private static final String BUCKET = "bucket";

    private final AmazonS3 client;

    public MinioS3Client(String endpoint, String accessKey, String secretKey) {
        this.client = getClient(endpoint, accessKey, secretKey);
    }

    public String getBucket() {
        return BUCKET;
    }

    public AmazonS3 getClient() {
        return client;
    }

    private AmazonS3 getClient(String endpoint, String accessKey, String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Override
    public void close() throws Exception {
        client.shutdown();
    }
}
