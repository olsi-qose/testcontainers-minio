package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes, UserDefinedType}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.testcontainers-minio.types.Frozen

/**
 * Wraps the array-schema returned by Jackson and contains the deduced corresponding
 * collection type.
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/collection_type_r.html
 * <p>
 * If a frozen collection type is needed, you must use `FrozenList`
 * or `FrozenSet`. if those don't suit the needs, any collection
 * type can be used so long as it implements the `Frozen`
 * interface.
 *
 * @author mridang
 */
class CassandraEntrySchema(val javaType: JavaType, var keyDataType: DataType, var valueDataType: DataType)
  extends ObjectSchema with CassandraType {

  val isFrozen: Boolean = classOf[Frozen].isAssignableFrom(javaType.getRawClass)

  override def getDataType: DataType = {
    // if the inner item is a UDT, it must be frozen or it will lead to this error
    // "Non-frozen UDTs are not allowed inside collections"
    keyDataType = keyDataType match {
      case x: UserDefinedType =>
        SchemaBuilder.udt(x.getName, true)
      case _ => keyDataType
    }

    // if the inner item is a UDT, it must be frozen or it will lead to this error
    // "Non-frozen UDTs are not allowed inside collections"
    valueDataType = valueDataType match {
      case x: UserDefinedType =>
        SchemaBuilder.udt(x.getName, true)
      case _ => valueDataType
    }

    DataTypes.mapOf(keyDataType, valueDataType)
  }

  override def getJavaType: JavaType = javaType

}
