package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.NumberSchema

/**
 * Wraps the number-schema returned by Jackson and contains the deduced corresponding
 * numeric type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cql_data_types_c.html
 *
 * @author mridang
 */
class CassandraNumberSchema(val javaType: JavaType, val dataType: DataType)
  extends NumberSchema with CassandraType {

  override def getDataType: DataType = dataType

  override def getJavaType: JavaType = javaType
}
