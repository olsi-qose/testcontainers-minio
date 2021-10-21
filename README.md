# TestContainers for MinIO

MinIO support for the test containers project.

## Installation

Unfortunately, TestContainers for MinIO is not available in any public Maven repositories except the GitHub Package Registry. For more information on how to install packages
from the GitHub Package
Registry, [https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package][see the GitHub docs]

## Usage

### Usage in Junit

MinIO can be easily used im JUnit via rules like any other test container. The snippet below shows how MinIO can be used in a Junit test.

```java
public class MinioBackedTest {

    @Rule
    public MinioContainer minio = new MinioContainer();

    @Test
    public void testWithContainer() {
        ...
    }
}
```

For advanced usages, read the docs at https://github.com/testcontainers/testcontainers-java


### Usage in ScalaTest

```scala
class MysqlSpec extends AnyFlatSpec with ForAllTestContainer {

  override val container: MinioContainer = MinioContainer()

  "MinIO container" should "be started" in {
     ...
  }
}
```

For advanced usages, read the docs at https://github.com/testcontainers/testcontainers-scala

## License

Apache-2.0 License

Copyright (c) 2021 Olsi Qose
Copyright (c) 2021 Mridang Agarwalla

[see the GitHub docs]: https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package
