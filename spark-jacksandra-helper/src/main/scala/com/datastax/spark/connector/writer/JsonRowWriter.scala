package com.datastax.spark.connector.writer

import com.datastax.spark.connector.JsonRowSupport

/**
 * Custom row-writer trait that can be used to decorate the existing
 * `RowWriter` implementations to denote support for serialising entities
 * to JSON.
 *
 * The basic `RowWriter` implementation maps accepts a bean and then prepares
 * a bound-staement by fetching properties one-by-one from the bean.
 *
 * @tparam T the type of the entity to serialize
 * @author mridang
 */
trait JsonRowWriter[T] extends JsonRowSupport[T] with Serializable {

  def toJson(row: T): String
}
