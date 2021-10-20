package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType

/**
 * Interface used by all wrapped schema objects so we can get both the Java type
 * and the corresponding Cassandra type for an schema node.
 *
 * @author mridang
 */
trait CassandraType {
  /**
   * Returns the deduced Cassandra type for the given schema node.
   * <p>
   * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cql_data_types_c.html
   *
   * @return the deduced Cassandra type
   */
  def getDataType: DataType

  def getJavaType: JavaType
}
