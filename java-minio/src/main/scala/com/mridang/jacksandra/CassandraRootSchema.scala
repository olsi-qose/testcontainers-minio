package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

/**
 * Wraps the object-schema returned by Jackson and contains the deduced corresponding
 * user-defined type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cqlRefUDType.html
 *
 * @author mridang
 */
class CassandraRootSchema(override val javaType: JavaType, override val backing: ObjectSchema, override val name: CqlName)
  extends CassandraUdtSchema(javaType, backing, name) {

  def getTableName: String = name.value
}
