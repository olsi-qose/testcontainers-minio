package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.codec.registry.CodecRegistry
import com.datastax.oss.driver.api.core.`type`.reflect.GenericType
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.fasterxml.jackson.databind.JavaType

class RegistryBasedCodecProvider(codecRegistry: CodecRegistry) extends CodecProvider {

  override def getDT(javaType: JavaType): DataType = {
    val xx: Class[_] = javaType.getRawClass
    val udtName = Option(xx.getAnnotation(classOf[CqlName]))
    udtName match {
      case Some(name) => QueryBuilder.udt(name.value())
      case None =>
        try {
          val gt: GenericType[_] = GenericType.of(xx)
          codecRegistry.codecFor(gt).getCqlType
        } catch {
          case e: Exception => {
            println("Had an IOException trying to read that file" + e)
          }
            DataTypes.TEXT
        }
    }
  }
}
