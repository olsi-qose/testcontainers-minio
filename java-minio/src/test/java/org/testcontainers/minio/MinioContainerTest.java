package org.testcontainers.minio;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinioContainerTest {

    private static final String BUCKET_NAME = "bucket";
    private static final MinioContainer.Credentials CREDENTIALS = new MinioContainer.Credentials("accessKey", "secretKey");

    @SuppressWarnings("RedundantThrows")
    @Test
    public void testContainer() throws Exception {
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

    @SuppressWarnings("RedundantThrows")
    @Test
    public void testContainerWithDefaultCredentials() throws Exception {
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