package org.jeasy.random.randomizers.extras

import scala.collection.immutable

class ListRandomizer extends CollectionTypeRandomizer[immutable.List[_]] {

  override def getRandomValue(value: Array[AnyRef]): List[_] = {
    immutable.List(value: _*)
  }
}
