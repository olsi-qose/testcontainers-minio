package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.mridang.testcontainers-minio.JavaBeanWithUDT.SomeUDT
import com.mridang.testcontainers-minio.types.{FrozenList, FrozenSet}
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

@CqlName("myjavabeanwithudt")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithUDT
(

  @PartitionKey
  @CqlName("mypartitionkey")
  var myPartitionKey: String,

  @CqlName("toudt")
  var toUdt: SomeUDT,

  @CqlName("tofrozenudtlist")
  var toFrozenUdtList: FrozenList[SomeUDT],

  @CqlName("tofrozenudtset")
  var toFrozenUdtSet: FrozenSet[SomeUDT],

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
