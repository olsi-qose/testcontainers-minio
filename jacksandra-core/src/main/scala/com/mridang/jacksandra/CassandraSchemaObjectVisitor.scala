package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable
import com.fasterxml.jackson.databind.{BeanProperty, JavaType, SerializerProvider}
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.factories.{ObjectVisitor, VisitorContext, WrapperFactory}
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema.SingleItems
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.testcontainers-minio.annotations.{OrderedClusteringColumn, StaticColumn}

class CassandraSchemaObjectVisitor(
                                    provider: SerializerProvider,
                                    schema: ObjectSchema,
                                    wrapperFactory: WrapperFactory,
                                    visitorContext: VisitorContext)
  extends ObjectVisitor(provider, schema, wrapperFactory) {
  setVisitorContext(visitorContext)

  //noinspection RedundantBlock,ScalaStyle
  override def optionalProperty(prop: BeanProperty): Unit = {
    val ann = prop.getAnnotation(classOf[CqlName])

    val isPartitionKey: Option[PartitionKey] = Option(
      prop.getAnnotation(classOf[PartitionKey]))

    val clusteringColumn: Option[OrderedClusteringColumn] = Option(
      prop.getAnnotation(classOf[OrderedClusteringColumn]))

    val staticColumn: Option[StaticColumn] = Option(
      prop.getAnnotation(classOf[StaticColumn]))

    if (ann != null) {
      val ps = propertySchema(prop)

      ps match {
        case _: CassandraJsonSchemaBase =>
          schema.putOptionalProperty(ann.value(), ps)
        case a: CassandraArraySchema => {

          val inner = a.getItems match {
            case s: SingleItems =>
              s.getSchema match {
                case rootSchema: CassandraRootSchema => {
                  prop.getType.getRawClass match {
                    case collType
                      if classOf[java.util.List[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraListSchema(
                        ann,
                        rootSchema.backing,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case collType
                      if classOf[java.util.Set[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraSetSchema(
                        ann,
                        rootSchema.backing,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case _ => {
                      new CassandraObjectSchema(
                        ann,
                        rootSchema.backing,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                  }
                }
                case os: ObjectSchema => {
                  prop.getType.getRawClass match {
                    case collType
                      if classOf[java.util.List[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraListSchema(
                        ann,
                        os,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case collType
                      if classOf[java.util.Set[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraSetSchema(
                        ann,
                        os,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case _ => {
                      new CassandraObjectSchema(
                        ann,
                        os,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                  }
                }
                case schemaType: JsonSchema => {
                  prop.getType.getRawClass match {
                    case collType
                      if classOf[java.util.List[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraListSchema(
                        ann,
                        schemaType,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case collType
                      if classOf[java.util.Set[_]]
                        .isAssignableFrom(collType) => {
                      new CassandraSetSchema(
                        ann,
                        schemaType,
                        isPartitionKey,
                        clusteringColumn,
                        staticColumn)
                    }
                    case _ => {
                      throw new RuntimeException("wht")
                    }
                  }
                }
              }
            case null =>
              new CassandraAnnotatedSchema(
                ann,
                a.getDataType,
                isPartitionKey,
                clusteringColumn,
                staticColumn)
          }

          schema.putOptionalProperty(ann.value(), inner)
        }

        case os: CassandraEntrySchema => {
          val cassandraColumn = new CassandraMapSchema(
            ann,
            os,
            isPartitionKey,
            clusteringColumn,
            staticColumn)
          schema.putOptionalProperty(ann.value(), cassandraColumn)
        }

        //noinspection RedundantBlock
        case os: ObjectSchema => {
          val cassandraColumn = new CassandraObjectSchema(
            ann,
            os,
            isPartitionKey,
            clusteringColumn,
            staticColumn)
          schema.putOptionalProperty(ann.value(), cassandraColumn)
        }

        //noinspection RedundantBlock
        case cassandraSchema: CassandraType => {
          val cassandraType: DataType = cassandraSchema.getDataType
          val cassandraColumn = new CassandraColumnSchema(
            ann,
            cassandraType,
            isPartitionKey,
            clusteringColumn,
            staticColumn)
          schema.putOptionalProperty(ann.value(), cassandraColumn)
        }

        //noinspection RedundantBlock
        case _ => {
          val cassandraColumn = new CassandraAnnotatedSchema(
            ann,
            DataTypes.TEXT,
            isPartitionKey,
            clusteringColumn,
            staticColumn)
          schema.putOptionalProperty(ann.value(), cassandraColumn)
        }
      }
    }
  }

  /**
   * Not implemented as there has not been a use-case.
   * Throws to avoid accidenta use of the default implementation.
   */
  override def optionalProperty(
                                 name: String,
                                 handler: JsonFormatVisitable,
                                 propertyTypeHint: JavaType): Unit = {
    throw new NotImplementedError()
  }

  /**
   * Not implemented as there has not been a use-case.
   * Throws to avoid accidenta use of the default implementation.
   */
  override def property(prop: BeanProperty): Unit = {
    throw new NotImplementedError()
  }

  /**
   * Not implemented as there has not been a use-case.
   * Throws to avoid accidenta use of the default implementation.
   */
  override def property(
                         name: String,
                         handler: JsonFormatVisitable,
                         propertyTypeHint: JavaType): Unit = {
    throw new NotImplementedError()
  }
}
