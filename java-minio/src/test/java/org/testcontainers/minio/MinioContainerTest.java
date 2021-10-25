package org.testcontainers.minio;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MinioContainerTest {

    private static final String ACCESS_KEY = "accessKey";
    private static final String SECRET_KEY = "secretKey";
    private static final MinioContainer.Credentials CREDENTIALS = new MinioContainer.Credentials(ACCESS_KEY, SECRET_KEY);

    @Test
    public void testContainer() throws Exception {
        try ( final MinioContainer minioContainer = new MinioContainer(CREDENTIALS) ) {
            minioContainer.start();

            try (
                    final MinioS3Client minioS3Client = new MinioS3Client("http://" + minioContainer.getHostAddress(), ACCESS_KEY, SECRET_KEY)
            ) {
                AmazonS3 client = minioS3Client.getClient();
                Bucket bucket = client.createBucket(minioS3Client.getBucket());
                assertNotNull(bucket);
                assertEquals(minioS3Client.getBucket(), bucket.getName());

                List<Bucket> buckets = client.listBuckets();
                assertNotNull(buckets);
                assertEquals(1, buckets.size());
            }
        }
    }

}