package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.{CreateType, CreateTypeStart}
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

import scala.collection.JavaConverters.mapAsScalaMapConverter

class CassandraUDTs(keyspace: String) {

  def of(name: CqlName, schema: ObjectSchema): String = {
    val xx: CreateTypeStart = SchemaBuilder
      .createType(keyspace, name.value())
      .ifNotExists()

    var createType: CreateType = null
    schema
      .asInstanceOf[CassandraUdtSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .foreach { column =>
        createType = createType match {
          case table: CreateType => //noinspection RedundantBlock
          {
            table.withField(column.ann.value, column.cassandraType)
          }
          case _ => //noinspection RedundantBlock
          {
            xx.withField(column.ann.value, column.cassandraType)
          }
        }
      }

    createType.build().getQuery
  }
}
