package com.mridang.testcontainers-minio

import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SchemaGenerationSuite extends AnyFunSuite {

  final val defaultKeyspace: String = "testcontainers-minio"

  /**
   * Utility method to compare two queries by replacing all spaces and newlines
   * as one of the queries is formatted for readability by generated one isn't
   *
   * @param actualQuery   the actual hard-coded query to assert against
   * @param expectedQuery the generated query to assert with
   */
  def compare(actualQuery: String, expectedQuery: String): Unit = {
    val actualQueryNorm = actualQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")
    val expectedQueryNorm = expectedQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")

    actualQueryNorm shouldEqual expectedQueryNorm
  }

  test("that all numeric types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithNumbers](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString
    //noinspection ScalaStyle
    val query =
      """ CREATE
        |  TABLE
        | IF NOT
        | EXISTS testcontainers-minio.myjavabeanwithnumbers
        |      ( mypartitionkey           TEXT                 PRIMARY KEY
        |      , tobigdecimal             DECIMAL
        |      , toint                    INT
        |      , tobigint                 BIGINT
        |      , tovarint                 VARINT
        |      , tofloat                  FLOAT
        |      , totinyint                TINYINT
        |      , tosmallint               SMALLINT
        |      , todouble                 DOUBLE
        |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all collection types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithCollections](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString
    //noinspection ScalaStyle
    val query =
      """ CREATE
        |  TABLE
        | IF NOT
        | EXISTS testcontainers-minio.myjavabeanwithcollections
        |      ( mypartitionkey           TEXT                 PRIMARY KEY
        |      , tosetliststring          FROZEN<SET<TEXT>>
        |      , tofrozenliststring       FROZEN<LIST<TEXT>>
        |      , toimmutablesetstring     SET<TEXT>
        |      , toimmutableliststring    LIST<TEXT>
        |      , tosetstring              SET<TEXT>
        |      , toliststring             LIST<TEXT>
        |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all temporal types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithTemporal](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString
    //noinspection ScalaStyle
    val query =
      """CREATE
        | TABLE
        |IF NOT
        |EXISTS testcontainers-minio.myjavabeanwithtemporals
        |     ( mypartitionkey           TEXT                  PRIMARY KEY
        |     , tolocaldatetimetimestamp TIMESTAMP
        |     , toyearstring             INT
        |     , tolocaldatedate          DATE
        |     , toperioddaterange        TEXT
        |     , tozoneoffsetstring       TEXT
        |     , toyearmonthstring        TEXT
        |     , tolocaltimetime          TIME
        |     , tooffsettimestring       TEXT
        |     , toinstanttimestamp       TIMESTAMP
        |     , tomonthdaystring         TEXT
        |     , todatetimestamp          TIMESTAMP
        |     , tojavadurationduration   DURATION
        |     , totimestamptimestamp     TIMESTAMP
        |     , tooffsetdatetimestring   TEXT
        |     , tozoneidstring           TEXT
        |  )
        """.stripMargin

    compare(query, ddl)
  }

  test("that all custom types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithUDT](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString("\n")
    //noinspection ScalaStyle
    val query =
      """
        | CREATE
        |   TYPE
        | IF NOT
        | EXISTS testcontainers-minio.myudt
        |      ( somestring               TEXT
        |      , somedoubleset            FROZEN<SET<DOUBLE>>
        |      , somedouble               DOUBLE
        |      , somelonglist             FROZEN<LIST<BIGINT>>
        |      )
        |
        | CREATE
        |  TABLE
        | IF NOT
        | EXISTS testcontainers-minio.myjavabeanwithudt
        |      ( mypartitionkey            TEXT                PRIMARY KEY
        |      , toudt                     myudt
        |      , tofrozenudtlist           FROZEN<LIST<FROZEN<myudt>>>
        |      , tofrozenudtset            FROZEN<SET<FROZEN<myudt>>>
        |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all exotic types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithExotics](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString("\n")
    //noinspection ScalaStyle
    val query =
      """
        | CREATE
        |  TABLE
        | IF NOT
        | EXISTS testcontainers-minio.myjavabeanwithexotics
        |      ( mypartitionkey           TEXT                 PRIMARY KEY
        |      , touuid                   UUID
        |      , totimeuuid               TIMEUUID
        |      , toip                     INET
        |      , toblob                   BLOB
        |      , toduration               DURATION
        |      , toascii                  ASCII
        |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all map types are handled correctly") {
    val mapper = new CassandraMapper[JavaBeanWithMaps](defaultKeyspace)
    val ddl: String = mapper.toSchema.mkString("\n")
    //noinspection ScalaStyle
    val query =
      """
        | CREATE
        |   TYPE
        | IF NOT
        | EXISTS testcontainers-minio.myudt
        |      ( somestring               TEXT
        |      , somedoubleset            FROZEN<SET<DOUBLE>>
        |      , somedouble               DOUBLE
        |      , somelonglist             FROZEN<LIST<BIGINT>>
        |      )
        |
        | CREATE
        |  TABLE
        | IF NOT
        | EXISTS testcontainers-minio.myjavabeanwithmaps
        |      ( mypartitionkey                TEXT                 PRIMARY KEY
        |      , tostringfloatmap              MAP<TEXT, FLOAT>
        |      , toimmutablestringdoublemap    MAP<TEXT, DOUBLE>
        |      , tomapstringkeyfrozenudtval    MAP<TEXT, FROZEN<myudt>>
        |      )
      """.stripMargin

    compare(query, ddl)
  }
}
