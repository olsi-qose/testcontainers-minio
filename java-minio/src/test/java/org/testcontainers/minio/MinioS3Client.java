package org.testcontainers.minio;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class MinioS3Client implements AutoCloseable {

    private final AmazonS3 client;

    public MinioS3Client(String endpoint, MinioContainer.Credentials credentials) {
        this.client = getClient(endpoint, credentials);
    }

    public AmazonS3 getClient() {
        return client;
    }

    private AmazonS3 getClient(String endpoint, MinioContainer.Credentials containerCredentials) {
        AWSCredentials credentials = new BasicAWSCredentials(
                containerCredentials.getUsername(),
                containerCredentials.getPassword());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Override
    public void close() throws Exception {
        if (client != null) {
            client.shutdown();
        }
    }
}
