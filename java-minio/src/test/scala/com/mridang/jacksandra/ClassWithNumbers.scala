package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

@CqlName("myjavabeanwithnumbers")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithNumbers
(
  @PartitionKey
  @CqlName("mypartitionkey")
  var myPartitionKey: String,

  @CqlName("totinyint")
  var toTinyInt: Byte,

  @CqlName("tosmallint")
  var toSmallInt: Short,

  @CqlName("toint")
  var toInt: Int,

  @CqlName("tobigint")
  var toBigInt: Long,

  @CqlName("tovarint")
  var toVarInt: BigInt,

  @CqlName("tofloat")
  var toFloat: Float,

  @CqlName("todouble")
  var toDouble: Double,

  @CqlName("tobigdecimal")
  var toBigDecimal: BigDecimal,

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
