package com.datastax.spark.connector.rdd

import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.datasource.ScanHelper.CqlQueryParts
import com.datastax.spark.connector.datasource.{CassandraScanHelper, ScanHelper}
import com.datastax.spark.connector.rdd.partitioner.CassandraPartition
import com.datastax.spark.connector.rdd.partitioner.dht.Token
import com.datastax.spark.connector.util.CountingIterator
import com.datastax.spark.connector.{AllColumns, ColumnSelector}
import org.apache.spark.metrics.InputMetricsUpdater
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, SparkContext, TaskContext}

import java.io.IOException

abstract class AbstractCassandraRDD[ENTITY](
                                             sc: SparkContext,
                                             val keyspaceName: String,
                                             val tableName: String,
                                             val columnNames: ColumnSelector = AllColumns,
                                             val whereClause: CqlWhereClause = CqlWhereClause.empty,
                                             val maybeLimit: Option[CassandraLimit] = None,
                                             val clusteringOrder: Option[ClusteringOrder] = None,
                                             val readConf: ReadConf = ReadConf()
                                           )(implicit val classTag: reflect.ClassTag[ENTITY],
                                             implicit val connector: CassandraConnector = CassandraConnector(sc))
  extends RDD[ENTITY](sc, Seq.empty)
    with CassandraTableRowReaderProvider[ENTITY] {

  override def compute(split: Partition,
                       context: TaskContext): Iterator[ENTITY] = {
    import scala.language.existentials

    val partition =
      split.asInstanceOf[CassandraPartition[Any, _ <: Token[Any]]]
    val tokenRanges = partition.tokenRanges
    val metricsUpdater = InputMetricsUpdater(context, readConf)

    val columnNames = selectedColumnRefs.map(_.selectedAs).toIndexedSeq

    val scanner = connector.connectionFactory.getScanner(readConf,
      connector.conf,
      columnNames)

    val queryParts =
      CqlQueryParts(selectedColumnRefs,
        buildWhereClause(partition.index),
        maybeLimit,
        clusteringOrder)

    // Iterator flatMap trick flattens the iterator-of-iterator structure into
    // a single iterator flatMap on iterator is lazy, therefore a query for the
    // next token range is executed not earlier than all of the rows returned
    // by the previous query have been consumed
    val rowIterator: Iterator[ENTITY] = tokenRanges.iterator.flatMap { range =>
      try {
        val scanResult = CassandraScanHelper.fetchTokenRange(scanner,
          tableDef,
          queryParts,
          range,
          consistencyLevel,
          fetchSize)
        val iteratorWithMetrics =
          scanResult.rows.map(metricsUpdater.updateMetrics)
        val result =
          iteratorWithMetrics.map(rowReader.read(_, scanResult.metadata))
        result
      } catch {
        case t: Throwable =>
          throw new IOException(
            s"Exception during scan execution for $range : ${t.getMessage}",
            t)
      }
    }
    val countingIterator: CountingIterator[ENTITY] =
      new CountingIterator(rowIterator,
        CassandraLimit.limitForIterator(maybeLimit))

    context.addTaskCompletionListener { context =>
      val duration = metricsUpdater.finish() / 1000000000d
      logDebug(
        f"Fetched ${countingIterator.count} rows from $keyspaceName.$tableName " +
          f"for partition ${partition.index} in $duration%.3f s.")
      scanner.close()
      context
    }
    countingIterator
  }

  def buildWhereClause(index: Int): CqlWhereClause

  def buildWhereClause(merchant: String): CqlWhereClause

  override protected def getPartitions: Array[Partition] = {
    ScanHelper
      .getPartitionGenerator(connector,
        tableDef,
        whereClause,
        minimalSplitCount,
        Option(1),
        splitSize)
      .partitions
      .toArray
  }

  def minimalSplitCount: Int = {
    context.defaultParallelism * 2 + 1
  }
}
