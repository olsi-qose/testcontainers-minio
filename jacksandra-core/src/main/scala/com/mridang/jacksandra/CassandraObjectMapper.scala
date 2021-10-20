package com.mridang.testcontainers-minio

import com.fasterxml.jackson.annotation.JsonFormat.{Shape, Value}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.cql.CassandraModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.time._

class CassandraObjectMapper extends ObjectMapper with Serializable {

  super.registerModule(DefaultScalaModule)
  super.registerModule(new Jdk8Module)
  super.registerModule(new JavaTimeModule)
  super.registerModule(new SimpleModule())
  super.registerModule(new CassandraModule)

  configOverride(classOf[OffsetDateTime]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[ZonedDateTime]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[Duration]).setFormat(Value.forShape(Shape.STRING)) // Use CqlDuration instead
  configOverride(classOf[LocalDateTime]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[LocalDate]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[LocalTime]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[MonthDay]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string (or array) and it is easy to query when it is a string
  configOverride(classOf[OffsetTime]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[Period]).setFormat(Value.forShape(Shape.STRING))
  configOverride(classOf[Year]).setFormat(Value.forShape(Shape.NUMBER_INT)) // Storing as number allows querying
  configOverride(classOf[YearMonth]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string (or array) and it is easy to query when it is a string
  configOverride(classOf[ZoneId]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string and it is easy to query when it is a string
  configOverride(classOf[ZoneOffset]).setFormat(Value.forShape(Shape.STRING))
}
