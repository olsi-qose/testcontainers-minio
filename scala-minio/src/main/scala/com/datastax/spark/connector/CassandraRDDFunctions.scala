package com.datastax.spark.connector

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.writer.{CassandraTableWriter, RowWriterFactory, WriteConf}
import com.fasterxml.jackson.databind.ObjectMapper
import com.mridang.testcontainers-minio.CassandraObjectMapper
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

class CassandraRDDFunctions[T](rdd: RDD[T])(implicit classTag: ClassTag[T])
  extends Serializable {

  final val tableName = classTag.runtimeClass.getAnnotation(classOf[CqlName]).value()
  val sparkContext: SparkContext = rdd.sparkContext

  def saveToCassandra(keyspaceName: String,
                      tableName: String = tableName,
                      objectMapper: () => ObjectMapper = () => new CassandraObjectMapper,
                      columns: ColumnSelector = AllColumns,
                      writeConf: WriteConf =
                      WriteConf.fromSparkConf(sparkContext.getConf))
                     (implicit connector: CassandraConnector = CassandraConnector(sparkContext), rwf: RowWriterFactory[T]):
  Unit = {

    val writer = CassandraTableWriter(connector, keyspaceName, tableName, columns, writeConf)
    rdd.sparkContext.runJob(rdd, writer.write _)
  }
}
