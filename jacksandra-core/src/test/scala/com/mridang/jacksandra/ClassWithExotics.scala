package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.mridang.testcontainers-minio.types.{CqlAscii, CqlBlob, CqlDuration, CqlTimeUUID}
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

import java.net.InetAddress
import java.util.UUID

@CqlName("myjavabeanwithexotics")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithExotics
(
  @PartitionKey
  @CqlName("mypartitionkey")
  var myPartitionKey: String,

  @CqlName("toascii")
  var toAscii: CqlAscii,

  @CqlName("toip")
  var toIP: InetAddress,

  @CqlName
  ("touuid") var toUUID: UUID,

  @CqlName("totimeuuid")
  var toTimeUUID: CqlTimeUUID,

  @CqlName("toblob")
  var toBlob: CqlBlob,

  @CqlName("toduration")
  var toDuration: CqlDuration,

) extends Serializable {

  override def hashCode: Int = {
    HashCodeBuilder.reflectionHashCode(this)
  }

  override def equals(obj: Any): Boolean = {
    EqualsBuilder.reflectionEquals(this, obj)
  }

  override def toString: String = {
    ToStringBuilder.reflectionToString(this)
  }
}
