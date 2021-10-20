package com.mridang.testcontainers-minio.javabeans

import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import com.datastax.oss.driver.internal.querybuilder.schema.DefaultCreateTable
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.mridang.testcontainers-minio.{CassandraJsonSchemaBase, CassandraRootSchema}

import scala.collection.JavaConverters
import scala.collection.JavaConverters.mapAsScalaMapConverter

//noinspection DuplicatedCode
class CassandraTable(keyspace: String, schema: JsonSchema) {

  //noinspection ScalaStyle
  def buildSchema: String = {
    val tableName = schema.asInstanceOf[CassandraRootSchema].getTableName
    val tableStart = SchemaBuilder
      .createTable(keyspace, tableName)
      .ifNotExists()

    var createTable: CreateTable = null
    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.isPartitionKey.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.isPartitionKey
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .foreach { column =>
        val cqlId = CqlIdentifier.fromInternal(column.ann.value)
        createTable = createTable match {
          case table: CreateTable => //noinspection RedundantBlock
          {
            table.withPartitionKey(cqlId, column.cassandraType)
          }
          case _ => //noinspection RedundantBlock
          {
            tableStart.withPartitionKey(cqlId, column.cassandraType)
          }
        }
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.clusteringColumn.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.clusteringColumn
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .foreach { column =>
        val cqlId = CqlIdentifier.fromInternal(column.ann.value)
        createTable = createTable.withClusteringColumn(cqlId, column.cassandraType)
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.staticColumn.nonEmpty
      }
      .foreach { column =>
        val cqlId = CqlIdentifier.fromInternal(column.ann.value)
        createTable =
          createTable.withStaticColumn(cqlId, column.cassandraType)
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.isPartitionKey.isEmpty
      }
      .filter { column =>
        column.clusteringColumn.isEmpty
      }
      .filter { column =>
        column.staticColumn.isEmpty
      }
      .foreach { column =>
        createTable match {
          case _: DefaultCreateTable => //noinspection RedundantBlock
          {
            val cqlId = CqlIdentifier.fromInternal(column.ann.value)
            createTable =
              createTable.withColumn(cqlId, column.cassandraType)
          }
          case _ =>
            throw new Exception("No partition keys specified")
        }
      }

    // Build the clustering order statement by first finding all properties and
    // then getting the sort order
    val clusteringOrder: Map[CqlIdentifier, ClusteringOrder] = schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.clusteringColumn.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.clusteringColumn
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .map((k: CassandraJsonSchemaBase) => {
        val cqlId = CqlIdentifier.fromInternal(k.ann.value)
        val clusteringOrder = k.clusteringColumn match {
          case Some(value) => //noinspection RedundantBlock
          {
            if (value.isAscending) {
              ClusteringOrder.ASC
            } else {
              ClusteringOrder.DESC
            }
          }
          case None => ClusteringOrder.ASC
        }
        (cqlId, clusteringOrder)
      })
      .toMap

    createTable.withClusteringOrderByIds(JavaConverters.mapAsJavaMap(clusteringOrder)).asCql()
  }
}
