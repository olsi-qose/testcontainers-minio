package com.datastax.spark.connector.datasource

import com.datastax.oss.driver.api.core.ConsistencyLevel
import com.datastax.oss.driver.api.core.CqlIdentifier.fromInternal
import com.datastax.spark.connector.ColumnRef
import com.datastax.spark.connector.cql.{ScanResult, Scanner, TableDef}
import com.datastax.spark.connector.datasource.ScanHelper.{CqlQueryParts, containsPartitionKey, partitionKeyStr, prepareScanStatement}
import com.datastax.spark.connector.rdd.CassandraLimit.limitToClause
import com.datastax.spark.connector.rdd.partitioner.CqlTokenRange
import com.datastax.spark.connector.util.Logging

object CassandraScanHelper extends Logging {

  def fetchTokenRange(scanner: Scanner,
                      tableDef: TableDef,
                      queryParts: CqlQueryParts,
                      range: CqlTokenRange[_, _],
                      consistencyLevel: ConsistencyLevel,
                      fetchSize: Int): ScanResult = {

    val session = scanner.getSession()

    val (cql, values) = tokenRangeToCqlQuery(range, tableDef, queryParts)

    logDebug(
      s"Fetching data for range ${range.cql(partitionKeyStr(tableDef))} " +
        s"with $cql " +
        s"with params ${values.mkString("[", ",", "]")}")

    val stmt = prepareScanStatement(session, cql, values: _*)
      .setConsistencyLevel(consistencyLevel)
      .setPageSize(fetchSize)
      .setRoutingToken(range.range.startNativeToken())

    val scanResult = scanner.scan(stmt)
    logDebug(
      s"Row iterator for range ${range.cql(partitionKeyStr(tableDef))} obtained successfully.")

    scanResult
  }

  def tokenRangeToCqlQuery(
                            range: CqlTokenRange[_, _],
                            tableDef: TableDef,
                            cqlQueryParts: CqlQueryParts): (String, Seq[Any]) = {

    val columns = cqlQueryParts.selectedColumnRefs.map((ref: ColumnRef) => ref.cql).mkString(", ")

    val (cql, values) =
      if (containsPartitionKey(tableDef, cqlQueryParts.whereClause)) {
        ("", Seq.empty)
      } else {
        range.cql(partitionKeyStr(tableDef))
      }
    val filter = (cql +: cqlQueryParts.whereClause.predicates)
      .filter(_.nonEmpty)
      .mkString(" AND ")
    val limitClause = limitToClause(cqlQueryParts.limitClause)
    val orderBy =
      cqlQueryParts.clusteringOrder.map(_.toCql(tableDef)).getOrElse("")
    val keyspaceName = fromInternal(tableDef.keyspaceName)
    val tableName = fromInternal(tableDef.tableName)
    val queryTemplate =
      s"SELECT JSON $columns " +
        s"FROM ${keyspaceName.asCql(true)}.${tableName.asCql(true)} " +
        s"WHERE $filter $orderBy $limitClause ALLOW FILTERING"
    val queryParamValues = values ++ cqlQueryParts.whereClause.values
    (queryTemplate, queryParamValues)
  }
}
