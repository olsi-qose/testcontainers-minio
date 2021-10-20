package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.annotation.{JsonSetter, Nulls}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

import java.io.Serializable
import javax.annotation.Nullable
import scala.collection.{immutable, mutable}

@SuppressWarnings(Array("unused"))
@CqlName("myjavabeanwithmaps")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithMaps
(
  @PartitionKey
  @CqlName("mypartitionkey")
  val myPartitionKey: String,

  @Nullable
  @CqlName("tostringfloatmap")
  var toStringFloatMap: mutable.Map[String, String],

  @Nullable
  @CqlName("toimmutablestringdoublemap")
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  var toImmutableStringDoubleMap: immutable.Map[String, String]

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
