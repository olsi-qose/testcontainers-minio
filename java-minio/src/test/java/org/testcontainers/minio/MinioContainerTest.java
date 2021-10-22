package org.testcontainers.minio;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.junit.After;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;

import static org.junit.Assert.*;

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
        } catch (Exception e) {
            fail();
        }
    }

    private AmazonS3 getS3Client(MinioContainer container) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + container.getHostAddress(), Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}