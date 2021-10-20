package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.mridang.testcontainers-minio.annotations.{OrderedClusteringColumn, StaticColumn}

/**
 * A subset of Cassandra mapping parameters, see
 * [[https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-params.html]]
 * Only params needed so far have been implemented.
 */
class CassandraAnnotatedSchema(
                                override val ann: CqlName,
                                override val cassandraType: DataType,
                                override val isPartitionKey: Option[PartitionKey],
                                override val clusteringColumn: Option[OrderedClusteringColumn],
                                override val staticColumn: Option[StaticColumn])
  extends CassandraJsonSchemaBase(
    ann,
    isPartitionKey,
    clusteringColumn,
    staticColumn)
