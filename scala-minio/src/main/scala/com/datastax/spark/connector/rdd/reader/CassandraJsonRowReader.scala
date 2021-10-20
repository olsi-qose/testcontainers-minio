package com.datastax.spark.connector.rdd.reader

import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.spark.connector.cql.TableDef
import com.datastax.spark.connector.{CassandraRowMetadata, ColumnName, ColumnRef}
import com.fasterxml.jackson.databind.ObjectMapper
import com.mridang.testcontainers-minio.CassandraObjectMapper

import java.nio.ByteBuffer
import scala.reflect.ClassTag

/**
 * A Jackson-backed row reader that deserializes JSON to entities. Surprisingly
 * Cassandra does support selecting JSON directly.
 *
 * https://cassandra.apache.org/doc/latest/cql/json.html#select-json
 *
 * With SELECT statements, the JSON keyword can be used to return each row as
 * a single JSON encoded map. The remainder of the SELECT statement behavior
 * is the same.
 *
 * The result map keys are the same as the column names in a normal result set.
 * For example, a statement like SELECT JSON a, ttl(b) FROM ... would result
 * in a map with keys "a" and "ttl(b)". However, this is one notable exception:
 * for symmetry with INSERT JSON behavior, case-sensitive column names with
 * upper-case letters will be surrounded with double quotes. For example,
 * SELECT JSON myColumn FROM ... would result in a map key "\"myColumn\""
 * (note the escaped quotes).
 *
 * The map values will JSON-encoded representations (as described below) of the
 * result set values.
 *
 * @param ct the evidence of the class
 * @tparam T the type of the entity to serialize
 * @author mridang
 */
class CassandraJsonRowReader[T](@transient override val objectMapper: () => ObjectMapper)
                               (implicit override val ct: ClassTag[T])
  extends RowReader[T]
    with JsonRowReader[T]
    with Serializable {

  override def neededColumns: Option[Seq[ColumnRef]] = {
    Option(columnNames.map(ColumnName(_)))
  }

  override def fromJson(row: Row, rowMetaData: CassandraRowMetadata): T = {
    read(row, rowMetaData)
  }

  /**
   * Reads the raw row bytes using an unsafe access. The unsafe access method
   * returns the raw byte buffer with the data and skips any and all coded-ops
   * or an transformation of the data.
   *
   * Using convenience methods like `getString` on the row objects leads to the
   * cell data to be processed by a `TypeCodec`. We're using Jackson, we don't
   * need that.
   *
   * The invocation of the `.array()` method on the byte-buffer returns the
   * backing byte array and it is a zero-copy op i.e. hyper performant.
   *
   * @author mridang
   */
  override def read(row: Row, rowMetaData: CassandraRowMetadata): T = {
    val rowBytes: ByteBuffer = row.getBytesUnsafe(0)
    objectMapper.apply().readValue(rowBytes.array(),
      ct.runtimeClass.asInstanceOf[Class[T]])
  }
}

/**
 * Factory class for generating the JSON-supporting `RowReader` instances.
 *
 * @param ct the internal class-tag evidence for building the type-reference
 * @tparam T the type of the entity to serialize
 */
class CassandraJsonRowReaderFactory[T](val objectMapper: () => ObjectMapper = () => new CassandraObjectMapper)
                                      (implicit val ct: ClassTag[T])
  extends RowReaderFactory[T]
    with Serializable {

  override def rowReader(table: TableDef,
                         columns: IndexedSeq[ColumnRef]): RowReader[T] = {
    new CassandraJsonRowReader[T](objectMapper)
  }

  override def targetClass: Class[T] = ct.runtimeClass.asInstanceOf[Class[T]]
}
