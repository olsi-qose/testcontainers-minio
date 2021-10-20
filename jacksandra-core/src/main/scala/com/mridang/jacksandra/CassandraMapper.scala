package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.codec.registry.CodecRegistry
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.internal.core.`type`.codec.extras.CassandraCodecRegistry
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.jsonSchema.factories._

import java.util
import scala.reflect.ClassTag

/**
 * A class for mapping scala pojos to elasticsearch query compatible objects.
 * The mapping is marked using [[CqlName]] annotations. Note that
 * we rely on java reflection instead of scala reflection since the scala API
 * still seems pretty experimental. Especially fetching annotation values in
 * a robust way turned out to be a nightmare.
 *
 * This class can do the following:
 * - Generate a map from the given class that can be serialized to JSON and used in Cassandra mapping definitions.
 * - Generate a map from a given instance that can be serialized to JSON and used in Cassandra index queries.
 *
 * @tparam T The class to be mapped
 */
class CassandraMapper[T](keyspace: String, codecProvider: CodecProvider)(implicit classTag: ClassTag[T]) {

  private val schemaMapper = new CassandraObjectMapper()

  def this(keyspace: String, codecRegistry: CodecRegistry)(implicit classTag: ClassTag[T]) {
    this(keyspace, new RegistryBasedCodecProvider(codecRegistry))(classTag)
  }

  def this(keyspace: String)(implicit classTag: ClassTag[T]) {
    this(keyspace, new CassandraCodecRegistry())(classTag)
  }

  def toSchema: List[String] = {
    val schemaFactoryWrapper: SchemaFactoryWrapper = {
      wrapperFactory.getWrapper(schemaMapper.getSerializerProvider)
    }

    schemaMapper.acceptJsonFormatVisitor(classTag.runtimeClass, schemaFactoryWrapper)
    CassandraSchema.from(keyspace, schemaFactoryWrapper)
  }

  def wrapperFactory: CassandraSchemaFactoryWrapperFactory = {
    new CassandraSchemaFactoryWrapperFactory((provider: SerializerProvider, factory: WrapperFactory) => {
      new CassandraSchemaFactoryWrapper(provider, factory, codecProvider)
    })
  }

  def map(obj: T): util.Map[String, _] = {
    schemaMapper.convertValue(obj, classOf[util.Map[String, Any]])
  }

  def toJson(obj: T): String = schemaMapper.writeValueAsString(obj)
}
