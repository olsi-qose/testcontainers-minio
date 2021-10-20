package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema

class CassandraBooleanSchema(val javaType: JavaType, val dataType: DataType)
  extends BooleanSchema with CassandraType {

  override def getDataType: DataType = dataType

  override def getJavaType: JavaType = javaType
}
