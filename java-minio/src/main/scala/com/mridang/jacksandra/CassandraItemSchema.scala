package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

import scala.annotation.meta.field

class CassandraItemSchema(@(JsonIgnore@field) val backing: ObjectSchema)
  extends ObjectSchema {

  override def getType: JsonFormatTypes =
    JsonFormatTypes.ANY // the value is irrelevant

  def getDataType: DataType = {
    backing match {
      case cassandraColumn: CassandraType => //noinspection RedundantBlock
      {
        cassandraColumn.getDataType
      }
      case _ => DataTypes.TEXT
    }
  }
}
