package com.datastax.spark.connector.rdd.reader

import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.spark.connector.{CassandraRowMetadata, JsonRowSupport}

/**
 * Custom row-reader trait that can be used to decorate the existing
 * `RowReader` implementations to denote support for deserialising JSON
 * to entities.
 *
 * The basic `RowReader` implementation maps reads maps entities by reading
 * values (by key) from the resultant Row.
 *
 * @tparam T the type of the entity to deserialize
 * @author mridang
 */
trait JsonRowReader[T] extends JsonRowSupport[T] with Serializable {

  def fromJson(row: Row, rowMetaData: CassandraRowMetadata): T

}
