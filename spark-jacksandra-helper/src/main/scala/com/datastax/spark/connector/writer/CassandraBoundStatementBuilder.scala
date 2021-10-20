package com.datastax.spark.connector.writer

import com.datastax.oss.driver.api.core.ProtocolVersion
import com.datastax.oss.driver.api.core.context.DriverContext
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement}

class CassandraBoundStatementBuilder[T](
                                         override val rowWriter: RowWriter[T],
                                         override val preparedStmt: PreparedStatement,
                                         val driverContext: DriverContext,
                                         override val prefixVals: Seq[Any] = Seq.empty,
                                         override val ignoreNulls: Boolean = false,
                                         override val protocolVersion: ProtocolVersion)
  extends BoundStatementBuilder[T](
    new RowWriter[T] {
      override def columnNames: Seq[String] = Seq("jsondata")

      override def readColumnValues(data: T, buffer: Array[Any]): Unit = ???
    },
    preparedStmt,
    prefixVals,
    ignoreNulls,
    protocolVersion
  ) {

  //noinspection UnstableApiUsage
  override def bind(row: T): RichBoundStatementWrapper = {
    val boundStatement = rowWriter match {
      case jsonRowWriter: JsonRowWriter[T] => //noinspection RedundantBlock
      {
        val json: String = jsonRowWriter.toJson(row)
        val singleStatement: BoundStatement = preparedStmt.bind(json)
        val cc = new RichBoundStatementWrapper(singleStatement)
        cc.bytesCount = singleStatement.computeSizeInBytes(driverContext)
        cc
      }
      case _ => super.bind(row)
    }

    boundStatement
  }
}
