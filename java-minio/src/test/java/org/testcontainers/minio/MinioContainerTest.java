package org.testcontainers.minio;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.junit.After;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MinioContainerTest {

    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final String BUCKET = "bucket";

    @Nullable
    private AmazonS3 client;

    @After
    public void shutDown() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }

    @Test
    public void testContainer() {
        try (
                final MinioContainer minioContainer = new MinioContainer(new MinioContainer.Credentials(ACCESS_KEY, SECRET_KEY));
        ) {
            minioContainer.start();
            client = getS3Client(minioContainer);
            Bucket bucket = client.createBucket(BUCKET);
            assertNotNull(bucket);
            assertEquals(BUCKET, bucket.getName());

            List<Bucket> buckets = client.listBuckets();
            assertNotNull(buckets);
            assertEquals(1, buckets.size());
        }
    }

    private AmazonS3 getS3Client(MinioContainer container) {
        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder.standard();

        String serviceEndpoint = "http://" + container.getHostAddress();
        clientBuilder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, "us-east-1"));
        clientBuilder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)));
        return clientBuilder.build();
    }
}