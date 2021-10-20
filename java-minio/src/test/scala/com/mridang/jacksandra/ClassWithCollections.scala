package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.annotation.{JsonSetter, Nulls}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.google.common.collect.{ImmutableList, ImmutableSet}
import com.mridang.testcontainers-minio.types.{FrozenList, FrozenSet}
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

import java.io.Serializable

@CqlName("myjavabeanwithcollections")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithCollections
(
  @PartitionKey
  @CqlName("mypartitionkey")
  val myPartitionKey: String,

  @CqlName("tofrozenliststring")
  val toFrozenListString: FrozenList[String],

  @CqlName("toliststring")
  val toListString: List[String],

  @CqlName("toimmutableliststring")
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  val toImmutableListString: ImmutableList[String],

  @CqlName("tosetliststring")
  val toSetListString: FrozenSet[String],

  @CqlName("tosetstring")
  val toSetString: Set[String],

  @CqlName("toimmutablesetstring")
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  val toImmutableSetString: ImmutableSet[String],
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

