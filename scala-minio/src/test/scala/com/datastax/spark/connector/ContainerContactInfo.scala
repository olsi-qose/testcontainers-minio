package com.datastax.spark.connector

import com.datastax.spark.connector.cql.IpBasedContactInfo
import org.testcontainers.containers

import java.net.InetSocketAddress

class ContainerContactInfo(container: containers.CassandraContainer[_])
  extends IpBasedContactInfo(hosts = {
    val host: String = container.getContainerIpAddress
    val port: Integer = container.getFirstMappedPort
    Set(new InetSocketAddress(host, port))
  })
