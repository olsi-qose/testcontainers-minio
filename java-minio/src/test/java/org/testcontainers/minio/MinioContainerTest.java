package org.testcontainers.minio;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

public class MinioContainerTest {

    private static final String BUCKET_NAME = "bucket";
    private static final MinioContainer.Credentials CREDENTIALS = new MinioContainer.Credentials("accessKey", "secretKey");

    @Test
    public void testContainer() {
        try (final MinioContainer minioContainer = new MinioContainer(CREDENTIALS)) {
            minioContainer.start();

            try (
                    final MinioS3Client minioS3Client = new MinioS3Client(minioContainer.getHttpHostAddress(), CREDENTIALS)
            ) {
                AmazonS3 client = minioS3Client.getClient();
                Bucket bucket = client.createBucket(BUCKET_NAME);
                assertEquals(BUCKET_NAME, bucket.getName());

                List<Bucket> buckets = client.listBuckets();
                assertEquals(1, buckets.size());
            }
        }
    }

    @Test
    public void testContainerWithDefaultCredentials() {
        try (final MinioContainer minioContainer = new MinioContainer()) {
            minioContainer.start();

            try (
                    final MinioS3Client minioS3Client = new MinioS3Client(minioContainer.getHttpHostAddress(), MinioContainer.Credentials.DEFAULT)
            ) {
                AmazonS3 client = minioS3Client.getClient();
                Bucket bucket = client.createBucket(BUCKET_NAME);
                assertEquals(BUCKET_NAME, bucket.getName());

                List<Bucket> buckets = client.listBuckets();
                assertEquals(1, buckets.size());
            }
        }
    }

}