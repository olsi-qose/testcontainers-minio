package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateType
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

import scala.collection.convert.DecorateAsScala

/**
 * Maps container types to Cassandra types. Otherwise Jackson deduces the type information
 * as [[JsonSchema]] types which will lead to wrong [[com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver]]
 */
trait CassandraContainerSchema
  extends CassandraJsonSchemaBase
    with DecorateAsScala {

  val name: CqlName

  val backing: JsonSchema

  val udt: Boolean = backing.isObjectSchema

  def fe: SimpleStatement = {
    val sche: ObjectSchema = backing.asObjectSchema()
    val query = SchemaBuilder.createType(name.value()).ifNotExists()
    sche.getProperties.asScala.foreach(g => {
      query.withField(g._1, DataTypes.TEXT)
    })
    query.asInstanceOf[CreateType].build()
  }

  def getProperties: String =
    backing.toString
}
