package com.datastax.spark.connector

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.language.implicitConversions
import scala.reflect.ClassTag

package object plus {

  implicit def toRDDFunctions[T](rdd: RDD[T])(implicit classTag: ClassTag[T]): CassandraRDDFunctions[T] =
    new CassandraRDDFunctions[T](rdd)

  implicit def toSparkContextFunctions(sc: SparkContext): JacksandraSparkContextFunctions =
    new JacksandraSparkContextFunctions(sc)
}
