package com.mridang.testcontainers-minio

import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.module.jsonSchema.factories.{SchemaFactoryWrapper, VisitorContext, WrapperFactory}

/**
 * Allows overwriting expectXFormat methods by instantiating sublasses of
 * [[SchemaFactoryWrapper]] instead of the default implementations.
 */
class CassandraSchemaFactoryWrapperFactory(wrapperFactory: (SerializerProvider, WrapperFactory) => SchemaFactoryWrapper)
  extends WrapperFactory {
  override def getWrapper(
                           provider: SerializerProvider): SchemaFactoryWrapper = {
    wrapperFactory(provider, this)
  }

  override def getWrapper(
                           provider: SerializerProvider,
                           rvc: VisitorContext): SchemaFactoryWrapper = {
    wrapperFactory(provider, this).setVisitorContext(rvc)
  }
}
