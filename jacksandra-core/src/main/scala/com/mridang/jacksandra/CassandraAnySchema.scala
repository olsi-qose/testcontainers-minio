package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema

/**
 * Wraps the any-schema returned by Jackson and contains the deduced corresponding
 * type.
 * <p>
 * This is used rarely as I'm not sure what comprises any "any" type.
 *
 * @author mridang
 */
class CassandraAnySchema(val javaType: JavaType, val dataType: DataType)
  extends AnySchema with CassandraType {

  override def getDataType: DataType = dataType

  override def getJavaType: JavaType = javaType
}
