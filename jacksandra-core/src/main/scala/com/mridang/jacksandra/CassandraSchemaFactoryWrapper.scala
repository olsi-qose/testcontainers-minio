package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.databind.`type`.{CollectionLikeType, MapLikeType}
import com.fasterxml.jackson.databind.jsonFormatVisitors._
import com.fasterxml.jackson.databind.{JavaType, SerializerProvider}
import com.fasterxml.jackson.module.jsonSchema.factories.{ObjectVisitor, SchemaFactoryWrapper, VisitorContext, WrapperFactory}
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

/**
 * A customized visitor that intercepts generated [[JsonSchema]] instances
 * and uses [[CassandraJsonSchemaBase]] based objects instead.
 */
class CassandraSchemaFactoryWrapper
(
  _provider: SerializerProvider,
  _wrapperFactory: WrapperFactory,
  codecProvider: CodecProvider
) extends SchemaFactoryWrapper(_provider, _wrapperFactory) {

  def cassandraVisitorContext: VisitorContext = {
    visitorContext
  }

  override def expectMapFormat(convertedType: JavaType):
  JsonMapFormatVisitor = {
    val jsonFormatVisitor: JsonMapFormatVisitor = super.expectMapFormat(convertedType)
    this.schema = new CassandraEntrySchema(
      convertedType,
      codecProvider.getDT(convertedType.asInstanceOf[MapLikeType].getKeyType),
      codecProvider.getDT(convertedType.asInstanceOf[MapLikeType].getContentType))
    jsonFormatVisitor
  }

  override def expectAnyFormat(convertedType: JavaType):
  JsonAnyFormatVisitor = {
    val anySchema = new CassandraAnySchema(convertedType, codecProvider.getDT(convertedType))
    this.schema = anySchema
    visitorFactory.anyFormatVisitor(anySchema)
  }

  /**
   * Returns a visitor when the bean properties are being traversed and and an
   * collection-like is found.
   *
   * Since Cassandra supports more tha just integers, we return a custom
   * `JsonSchema` that will contain the correct `DataType`.
   *
   * <ul>
   * <li>`DataTypes.listOf`</li>
   *
   * <ul>
   */
  override def expectArrayFormat(convertedType: JavaType):
  JsonArrayFormatVisitor = {
    val jsonFormatVisitor: JsonArrayFormatVisitor = super.expectArrayFormat(convertedType)
    this.schema = new CassandraArraySchema(
      convertedType,
      codecProvider.getDT(convertedType.asInstanceOf[CollectionLikeType].getContentType))
    jsonFormatVisitor
  }

  /**
   * Returns a visitor when the bean properties are being traversed and and an
   * boolean is found.
   *
   * Since Cassandra supports more tha just integers, we return a custom
   * `JsonSchema` that will contain the correct `DataType`.
   *
   * <ul>
   * <li>`DataTypes.BOOLEAN`</li>
   * <ul>
   */
  override def expectBooleanFormat(convertedType: JavaType):
  JsonBooleanFormatVisitor = {
    val booleanSchema = new CassandraBooleanSchema(convertedType, codecProvider.getDT(convertedType))
    this.schema = booleanSchema
    visitorFactory.booleanFormatVisitor(booleanSchema)
  }

  /**
   * Returns a visitor when the bean properties are being traversed and and an
   * integer is found.
   *
   * Since Cassandra supports more tha just integers, we return a custom
   * `JsonSchema` that will contain the correct `DataType`.
   *
   * <ul>
   * <li>`DataTypes.BIGINT`</li>
   * <li>`DataTypes.DECIMAL`</li>
   * <li>`DataTypes.DOUBLE`</li>
   * <li>`DataTypes.FLOAT`</li>
   * <li>`DataTypes.INT`</li>
   * <li>`DataTypes.SMALLINT`</li>
   * <li>`DataTypes.TINYINT`</li>
   * <li>`DataTypes.VARINT`</li>
   * <ul>
   */
  override def expectIntegerFormat(convertedType: JavaType):
  JsonIntegerFormatVisitor = {
    val integerSchema = new CassandraIntegerSchema(convertedType, codecProvider.getDT(convertedType))
    this.schema = integerSchema
    visitorFactory.integerFormatVisitor(integerSchema)
  }

  /**
   * Returns a visitor when the bean properties are being traversed and and an
   * integer is found.
   *
   * Since Cassandra supports more tha just integers, we return a custom
   * `JsonSchema` that will contain the correct `DataType`.
   *
   * <ul>
   * <li>`DataTypes.BIGINT`</li>
   * <li>`DataTypes.DECIMAL`</li>
   * <li>`DataTypes.DOUBLE`</li>
   * <li>`DataTypes.FLOAT`</li>
   * <li>`DataTypes.INT`</li>
   * <li>`DataTypes.SMALLINT`</li>
   * <li>`DataTypes.TINYINT`</li>
   * <li>`DataTypes.VARINT`</li>
   * <ul>
   */
  override def expectNumberFormat(convertedType: JavaType):
  JsonNumberFormatVisitor = {
    val numberSchema = new CassandraNumberSchema(convertedType, codecProvider.getDT(convertedType))
    schema = numberSchema
    visitorFactory.numberFormatVisitor(numberSchema)
  }

  /**
   * Returns a visitor when the bean properties are being traversed and and an
   * string-like field is found.
   *
   * Since Cassandra supports more tha just just string, we return a custom
   * `JsonSchema` that will contain the correct `DataType`.
   *
   * <ul>
   * <li>`DataTypes.ASCII`</li>
   * <li>`DataTypes.TEXT`</li>
   * <li>`DataTypes.VARCHAR`</li>
   * <ul>
   */
  override def expectStringFormat(convertedType: JavaType):
  JsonStringFormatVisitor = {
    val stringSchema = new CassandraStringSchema(convertedType, codecProvider.getDT(convertedType))
    schema = stringSchema
    visitorFactory.stringFormatVisitor(stringSchema)
  }

  /**
   * Customised [[ObjectSchema]] visits:
   * - Disable reference schemas as there is no support for such things in Cassandra (so don't call visitorContext.addSeenSchemaUri)
   * - Put [[CassandraJsonSchemaBase]] based objects to schema instead of standard [[JsonSchema]] ones.
   */
  override def expectObjectFormat(convertedType: JavaType):
  JsonObjectFormatVisitor = {
    // if we don't already have a recursive visitor context, create one
    if (visitorContext == null) visitorContext = new CassandraVisitorContext

    val objectSchema: ObjectSchema = schemaProvider.objectSchema

    //noinspection SimplifyBooleanMatch
    visitorContext.asInstanceOf[CassandraVisitorContext].seenObjects match {
      //noinspection RedundantBlock
      case false => {
        val name = convertedType.getRawClass.getAnnotation(classOf[CqlName])
        schema = new CassandraRootSchema(convertedType, objectSchema, name)
        visitorContext.asInstanceOf[CassandraVisitorContext].seenObjects = true
      }
      case true => {
        val name = convertedType.getRawClass.getAnnotation(classOf[CqlName])
        val udtSchema = new CassandraUdtSchema(convertedType, objectSchema, name)
        schema = new CassandraItemSchema(udtSchema)
        visitorContext.asInstanceOf[CassandraVisitorContext].addSeenUDT(name, udtSchema)
        visitorContext.asInstanceOf[CassandraVisitorContext].seenObjects = true
      }
    }

    visitor(provider, objectSchema, _wrapperFactory, visitorContext)
  }

  def visitor(
               provider: SerializerProvider,
               schema: ObjectSchema,
               wrapperFactory: WrapperFactory,
               visitorContext: VisitorContext): ObjectVisitor =
    new CassandraSchemaObjectVisitor(
      provider,
      schema,
      wrapperFactory,
      visitorContext)
}
