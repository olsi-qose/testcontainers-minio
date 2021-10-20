package com.datastax.spark.connector

import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.`type`.codec.extras.scala.ScalaCodecRegistry
import com.datastax.spark.connector.SparkIntegrationSuite.objectMapper
import com.datastax.spark.connector.cql.{CassandraConnector, ContactInfo}
import com.datastax.spark.connector.writer.{CassandraJsonRowWriterFactory, RowWriterFactory}
import com.dimafeng.testcontainers.{CassandraContainer, ForAllTestContainer}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.holdenkarau.spark.testing.SharedSparkContext
import com.mridang.testcontainers-minio._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner
import org.testcontainers.utility.DockerImageName

import scala.reflect.ClassTag

object SparkIntegrationSuite {

  val objectMapper: ObjectMapper = {
    new CassandraObjectMapper().registerModule(new GuavaModule)
  }

}

@RunWith(classOf[JUnitRunner])
class SparkIntegrationSuite extends AnyFunSuite with ForAllTestContainer with SharedSparkContext {

  import com.datastax.spark.connector.plus.{toRDDFunctions, toSparkContextFunctions}

  final val defaultKeyspace: String = "testcontainers-minio"
  final val randomItemsCount: Int = 1
  override val conf: SparkConf = {
    super.conf.set("spark.cassandra.output.batch.grouping.key", "none")
  }
  override val container: CassandraContainer = CassandraContainer(DockerImageName.parse("cassandra:3.11.6"))

  def createTable[T]()(implicit ctag: ClassTag[T]): Unit = {
    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    CassandraConnector(contactInfo).withSessionDo(session => {
      val mapper = new CassandraMapper[T](defaultKeyspace, new ScalaCodecRegistry())

      session.execute(SchemaBuilder.dropKeyspace(defaultKeyspace).ifExists()
        .asCql())

      session.execute(SchemaBuilder.createKeyspace(defaultKeyspace).ifNotExists()
        .withSimpleStrategy(1)
        .asCql())

      val createSchema: List[String] = mapper.toSchema
      createSchema.foreach(statement => {
        session.execute(statement)
      })
    })
  }

  test("that saving and querying entities works as expected") {
    createTable[MyBean]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[MyBean] = new CassandraJsonRowWriterFactory[MyBean]

    val inputRDD: RDD[MyBean] = RandomRDD[MyBean](sc).of(randomItemsCount)
    val inputItems: Set[MyBean] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[MyBean](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with collections works as expected") {
    createTable[JavaBeanWithCollections]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithCollections] = new CassandraJsonRowWriterFactory[JavaBeanWithCollections]

    val inputRDD: RDD[JavaBeanWithCollections] = RandomRDD[JavaBeanWithCollections](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithCollections] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithCollections](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with numbers works as expected") {
    createTable[JavaBeanWithNumbers]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithNumbers] = new CassandraJsonRowWriterFactory[JavaBeanWithNumbers]

    val inputRDD: RDD[JavaBeanWithNumbers] = RandomRDD[JavaBeanWithNumbers](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithNumbers] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithNumbers](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with temporals works as expected") {
    createTable[JavaBeanWithTemporal]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithTemporal] = new CassandraJsonRowWriterFactory[JavaBeanWithTemporal]

    val inputRDD: RDD[JavaBeanWithTemporal] = RandomRDD[JavaBeanWithTemporal](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithTemporal] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithTemporal](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with udts works as expected") {
    createTable[JavaBeanWithUDT]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithUDT] = new CassandraJsonRowWriterFactory[JavaBeanWithUDT]

    val inputRDD: RDD[JavaBeanWithUDT] = RandomRDD[JavaBeanWithUDT](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithUDT] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithUDT](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with exotics works as expected") {
    createTable[JavaBeanWithExotics]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithExotics] = new CassandraJsonRowWriterFactory[JavaBeanWithExotics]

    val inputRDD: RDD[JavaBeanWithExotics] = RandomRDD[JavaBeanWithExotics](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithExotics] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithExotics](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with maps works as expected") {
    createTable[JavaBeanWithMaps]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[JavaBeanWithMaps] = new CassandraJsonRowWriterFactory[JavaBeanWithMaps]

    val inputRDD: RDD[JavaBeanWithMaps] = RandomRDD[JavaBeanWithMaps](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithMaps] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[JavaBeanWithMaps](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with collections works as expected") {
    createTable[ClassWithCollections]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithCollections] = new CassandraJsonRowWriterFactory[ClassWithCollections]

    val inputRDD: RDD[ClassWithCollections] = RandomRDD[ClassWithCollections](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithCollections] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithCollections](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with numbers works as expected") {
    createTable[ClassWithNumbers]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithNumbers] = new CassandraJsonRowWriterFactory[ClassWithNumbers]

    val inputRDD: RDD[ClassWithNumbers] = RandomRDD[ClassWithNumbers](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithNumbers] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithNumbers](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with temporals works as expected") {
    createTable[ClassWithTemporal]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithTemporal] = new CassandraJsonRowWriterFactory[ClassWithTemporal]

    val inputRDD: RDD[ClassWithTemporal] = RandomRDD[ClassWithTemporal](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithTemporal] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithTemporal](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with udts works as expected") {
    createTable[ClassWithUDT]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithUDT] = new CassandraJsonRowWriterFactory[ClassWithUDT]

    val inputRDD: RDD[ClassWithUDT] = RandomRDD[ClassWithUDT](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithUDT] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithUDT](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with exotics works as expected") {
    createTable[ClassWithExotics]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithExotics] = new CassandraJsonRowWriterFactory[ClassWithExotics]

    val inputRDD: RDD[ClassWithExotics] = RandomRDD[ClassWithExotics](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithExotics] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithExotics](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying scala classes with maps works as expected") {
    createTable[ClassWithMaps]()

    val contactInfo: ContactInfo = new ContainerContactInfo(container.container)
    implicit val connector: CassandraConnector = CassandraConnector(contactInfo)
    implicit val rwf: RowWriterFactory[ClassWithMaps] = new CassandraJsonRowWriterFactory[ClassWithMaps]

    val inputRDD: RDD[ClassWithMaps] = RandomRDD[ClassWithMaps](sc).of(randomItemsCount)
    val inputItems: Set[ClassWithMaps] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, objectMapper = () => objectMapper)
    sc.cassandraTable[ClassWithMaps](defaultKeyspace, objectMapper = () => objectMapper)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }
}
