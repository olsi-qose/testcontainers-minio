package org.jeasy.random.randomizers.guava

import com.google.common.collect.ImmutableMap
import org.jeasy.random.randomizers.extras.MapTypeRandomizer

import scala.jdk.CollectionConverters.mapAsJavaMapConverter

class ImmutableMapRandomizer extends MapTypeRandomizer[ImmutableMap[_, _]] {

  override def getRandomValue(value: Array[(Any, Any)]): ImmutableMap[_, _] = {
    ImmutableMap.copyOf[Any, Any](Map(value: _*).asJava)
  }
}
