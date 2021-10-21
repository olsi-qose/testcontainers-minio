package org.testcontainers.minio

import com.dimafeng.testcontainers.{ContainerDef, SingleContainer}
import org.testcontainers.minio.{MinioContainer => JavaMinioContainer}
import org.testcontainers.utility.DockerImageName

import java.net.InetSocketAddress

case class MinioContainer
(
  dockerImageName: DockerImageName = DockerImageName.parse(MinioContainer.defaultDockerImageName)
) extends SingleContainer[JavaMinioContainer] {

  override val container: JavaMinioContainer = new JavaMinioContainer(dockerImageName)

  def httpHostAddress: String = container.getHttpHostAddress

  def tcpHost: InetSocketAddress = container.getTcpHost

  underlyingUnsafeContainer
}

object MinioContainer {

  val defaultImage = "minio/minio"
  val defaultTag = "latest"
  val defaultDockerImageName = s"$defaultImage:$defaultTag"

  case class Def(
                  dockerImageName: DockerImageName = DockerImageName.parse(MinioContainer.defaultDockerImageName)
                ) extends ContainerDef {

    override type Container = MinioContainer

    override def createContainer(): MinioContainer = {
      new MinioContainer(
        dockerImageName
      )
    }
  }
}
