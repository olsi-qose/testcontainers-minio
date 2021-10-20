package org.jeasy.random.randomizers.extras

import scala.collection.{immutable, mutable}

object MapRandomizer {

  class Immutable extends MapTypeRandomizer[immutable.Map[_, _]] {

    override def getRandomValue(value: Array[(Any, Any)]): immutable.Map[_, _] = {
      immutable.Map(value: _*)
    }
  }

  class Mutable extends MapTypeRandomizer[mutable.Map[_, _]] {

    override def getRandomValue(value: Array[(Any, Any)]): mutable.Map[_, _] = {
      mutable.Map(value: _*)
    }
  }
}
