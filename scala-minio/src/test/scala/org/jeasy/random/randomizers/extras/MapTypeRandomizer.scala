package org.jeasy.random.randomizers.extras

import org.jeasy.random.EasyRandom
import org.jeasy.random.api.{ContextAwareRandomizer, RandomizerContext}
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer
import org.jeasy.random.util.ReflectionUtils

import java.lang.reflect.{Field, ParameterizedType, Type}

abstract class MapTypeRandomizer[T] extends ContextAwareRandomizer[T] {

  private var context: RandomizerContext = _

  override def setRandomizerContext(context: RandomizerContext): Unit = {
    this.context = context
  }

  def getRandomValue(value: Array[(Any, Any)]): T

  override def getRandomValue: T = {
    try {
      val field = context.getTargetType.getField(context.getCurrentField)
      getRandomValue(getRandomCollection(field))
    } catch {
      case _: NoSuchFieldException =>
        try {
          val field = context.getTargetType.getDeclaredField(context.getCurrentField)
          getRandomValue(getRandomCollection(field))
        }
        catch {
          case noSuchFieldException: NoSuchFieldException =>
            throw new RuntimeException(noSuchFieldException)
        }
    }
  }

  def getRandomCollection(field: Field): Array[(Any, Any)] = {
    val easyRandom = new EasyRandom(context.getParameters)
    val collectionSizeRange = context.getParameters.getCollectionSizeRange
    val randomSize = new IntegerRangeRandomizer(collectionSizeRange.getMin, collectionSizeRange.getMax, easyRandom.nextLong).getRandomValue

    //noinspection SimplifyBooleanMatch
    ReflectionUtils.isParameterizedType(field.getGenericType) match {
      case true =>
        val parameterizedType = field.getGenericType.asInstanceOf[ParameterizedType]
        val keyType: Type = parameterizedType.getActualTypeArguments()(0)
        val valueType: Type = parameterizedType.getActualTypeArguments()(1)
        (1 to randomSize)
          .map { _ =>
            val randomKey = easyRandom.nextObject(keyType.asInstanceOf[Class[_]])
            val randomValue = easyRandom.nextObject(valueType.asInstanceOf[Class[_]])
            (randomKey, randomValue)
          }
          .filter { pair => pair._1 != null }
          .toArray
      case false =>
        Array.empty
    }
  }
}
