package com.datastax.spark.connector

import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.{BeanDescription, JavaType, ObjectMapper}

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

/**
 * Custom row-writer/reader trait that can be used to decorate the existing
 * `RowReader` and `RowWriter` implementations to denote support for serde
 * between JSON and entities and vice versa.
 *
 * This class has some Jackson-specific foo to get a list of properties i.e.
 * columns, from the bean. You would think that with all the power of Jackson,
 * this would be a one-liner but nah. When introspecting beans to find a list
 * of properties, it returns ALL properties and does not take into account any
 * properties that are excluded e.g. via the `JsonIgnore` annotation.
 *
 * https://stackoverflow.com/q/45834654/304151
 *
 * @tparam T the type of the entity to serialise/deserialize
 * @author mridang
 */
trait JsonRowSupport[T] {

  val objectMapper: () => ObjectMapper

  val ct: ClassTag[T]

  def columnNames: Seq[String] = {
    val jType: JavaType =
      objectMapper.apply().getTypeFactory.constructType(ct.runtimeClass)
    val beanDesc: BeanDescription =
      objectMapper.apply().getSerializationConfig.introspect[BeanDescription](jType)
    val ignoredProps: Set[String] = getIgnoredProperties(beanDesc)
    val properties: Seq[String] = getDescribedProperties(beanDesc)
    properties diff ignoredProps.toList
  }

  private def getDescribedProperties(
                                      beanDescription: BeanDescription): List[String] = {
    beanDescription.findProperties.asScala
      .filter(propDef => propDef.getAccessor != null)
      .map(pr => pr.getName)
      .toList
  }

  private def getIgnoredProperties(
                                    beanDescription: BeanDescription): Set[String] = {
    val introspector =
      objectMapper.apply().getSerializationConfig.getAnnotationIntrospector
    val classInfo: AnnotatedClass = beanDescription.getClassInfo
    Option(introspector.findPropertyIgnorals(classInfo)) match {
      case Some(value) => value.getIgnored.asScala.toSet
      case None => Set.empty
    }
  }
}
