package com.datastax.spark.connector.writer

import com.datastax.spark.connector.ColumnRef
import com.datastax.spark.connector.cql.TableDef
import com.fasterxml.jackson.databind.ObjectMapper
import com.mridang.testcontainers-minio.CassandraObjectMapper

import scala.reflect.ClassTag

/**
 * A Jackson-backed row writer that serializes entities to JSON. Surprisingly
 * Cassandra does support inserting JSON directly.
 *
 * https://cassandra.apache.org/doc/latest/cql/json.html#insert-json
 *
 * With INSERT statements, the new JSON keyword can be used to enable inserting
 * a JSON encoded map as a single row. The format of the JSON map should
 * generally match that returned by a SELECT JSON statement on the same table.
 * In particular, case-sensitive column names should be surrounded with double quotes.
 * For example, to insert into a table with two columns named “myKey” and “value”,
 * you would do the following:
 * <pre>
 * INSERT INTO mytable JSON '{ "\"myKey\"": 0, "value": 0}'
 * </pre>
 *
 * By default (or if DEFAULT NULL is explicitly used), a column omitted from the
 * JSON map will be set to NULL, meaning that any pre-existing value for that
 * column will be removed (resulting in a tombstone being created).
 *
 * Alternatively, if the DEFAULT UNSET directive is used after the value,
 * omitted column values will be left unset, meaning that pre-existing values
 * for those column will be preserved.
 *
 * @param ct the evidence of the class
 * @tparam T the type of the entity to serialize
 * @author mridang
 */
class CassandraJsonRowWriter[T](override val objectMapper: () => ObjectMapper)
                               (implicit override val ct: ClassTag[T])
  extends RowWriter[T]
    with JsonRowWriter[T]
    with Serializable {

  //noinspection NotImplementedCode
  override def readColumnValues(data: T, buffer: Array[Any]): Unit = {
    ??? // Intentionally unimplemented so we don't accidentally use this.
  }

  override def toJson(row: T): String = {
    objectMapper.apply().writeValueAsString(row)
  }
}

/**
 * Factory class for generating the JSON-supporting `RowWriter` instances.
 *
 * There's nothing special here. Just a factory.
 *
 * @param ct the internal class-tag evidence for building the type-reference
 * @tparam T the type of the entity to serialize
 */
class CassandraJsonRowWriterFactory[T](val objectMapper: () => ObjectMapper = () => new CassandraObjectMapper)
                                      (implicit val ct: ClassTag[T])
  extends RowWriterFactory[T]
    with Serializable {

  override def rowWriter(table: TableDef,
                         columns: IndexedSeq[ColumnRef]): RowWriter[T] = {
    new CassandraJsonRowWriter[T](objectMapper)
  }
}
