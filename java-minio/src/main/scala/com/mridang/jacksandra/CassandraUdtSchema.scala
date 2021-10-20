package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

import java.util

/**
 * Wraps the object-schema returned by Jackson and contains the deduced corresponding
 * user-defined type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cqlRefUDType.html
 *
 * @author mridang
 */
class CassandraUdtSchema(val javaType: JavaType, val backing: ObjectSchema, val name: CqlName)
  extends ObjectSchema with CassandraType {

  override def getDataType: DataType = {
    QueryBuilder.udt(if (name != null) name.value
    else javaType.getRawClass.getSimpleName)
  }

  override def getJavaType: JavaType = javaType

  override def getProperties: util.Map[String, JsonSchema] = backing.getProperties
}
