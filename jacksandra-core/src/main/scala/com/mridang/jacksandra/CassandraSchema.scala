package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.testcontainers-minio.javabeans.CassandraTable

import scala.compat.Platform

object CassandraSchema {

  def from(keyspace: String, schemaFactoryWrapper: SchemaFactoryWrapper): List[String] = {
    val types: List[String] = schemaFactoryWrapper
      .asInstanceOf[CassandraSchemaFactoryWrapper]
      .cassandraVisitorContext
      .asInstanceOf[CassandraVisitorContext]
      .udts
      .map { (f: (CqlName, ObjectSchema)) =>
        new CassandraUDTs(keyspace).of(f._1, f._2) + Platform.EOL
      }
      .toList

    val table: String = new CassandraTable(keyspace, schemaFactoryWrapper.finalSchema())
      .buildSchema + Platform.EOL

    types ++ List(table)
  }
}
