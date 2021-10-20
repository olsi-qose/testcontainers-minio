package org.jeasy.random.randomizers.extras

import scala.collection.immutable

class SetRandomizer extends CollectionTypeRandomizer[immutable.Set[_]] {

  override def getRandomValue(value: Array[AnyRef]): Set[_] = {
    immutable.Set(value: _*)
  }
}
