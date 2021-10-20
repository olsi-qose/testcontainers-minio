package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.mridang.testcontainers-minio.annotations.{OrderedClusteringColumn, StaticColumn}

abstract class CassandraJsonSchemaBase(
                                        val ann: CqlName,
                                        val isPartitionKey: Option[PartitionKey],
                                        val clusteringColumn: Option[OrderedClusteringColumn],
                                        val staticColumn: Option[StaticColumn])
  extends JsonSchema {

  override def getType: JsonFormatTypes =
    JsonFormatTypes.ANY // the value is irrelevant

  def cassandraType: DataType
}
