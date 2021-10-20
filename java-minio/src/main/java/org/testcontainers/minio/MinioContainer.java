package org.testcontainers.minio;

import java.net.InetSocketAddress;
import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class MinioContainer extends GenericContainer<MinioContainer> {

    private static final int MINIO_INTERNAL_PORT = 9000;
    protected static final String DEFAULT_TAG = "RELEASE.2021-10-13T00-23-17Z.fips";
    private static final String DEFAULT_STORAGE_DIRECTORY = "/data";
    private static final String MINIO_ROOT_USER = "MINIO_ROOT_USER";
    private static final String MINIO_ROOT_PASSWORD = "MINIO_ROOT_PASSWORD";

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("quay.io/minio/minio");

    /**
     * Create a Minio Container by passing the full docker image name
     *
     * @param dockerImageName Full docker image name as a {@link String}, like: minio/minio:7.9.2
     */
    public MinioContainer(String dockerImageName) {
        this(Credentials.DEFAULT, DockerImageName.parse(dockerImageName));
    }

    public MinioContainer(DockerImageName dockerImageName) {
        this(Credentials.DEFAULT, dockerImageName);
    }

    public MinioContainer(Credentials credentials) {
        this(credentials, DEFAULT_IMAGE_NAME.withTag(DEFAULT_TAG));
    }

    public MinioContainer(Credentials credentials, DockerImageName dockerImageName) {
        super(dockerImageName);
        addExposedPorts(9000, 9001);
        if (credentials != null) {
            withEnv(MINIO_ROOT_USER, credentials.getUsername());
            withEnv(MINIO_ROOT_PASSWORD, credentials.getPassword());
        }
        withCommand("server", DEFAULT_STORAGE_DIRECTORY);
        setWaitStrategy(new HttpWaitStrategy()
                .forPort(MINIO_INTERNAL_PORT)
                .forPath(DEFAULT_STORAGE_DIRECTORY)
                .withStartupTimeout(Duration.ofMinutes(1)));
    }

    public String getHostAddress() {
        return getContainerIpAddress() + ":" + getMappedPort(MINIO_INTERNAL_PORT);
    }

    public String getHttpHostAddress() {
        return getHost() + ":" + getMappedPort(MINIO_INTERNAL_PORT);
    }

    public InetSocketAddress getTcpHost() {
        return new InetSocketAddress(getHost(), getMappedPort(MINIO_INTERNAL_PORT));
    }

    public static class Credentials {

        public static final Credentials DEFAULT = new Credentials("minio_username", "minio_password");
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

}