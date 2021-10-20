package com.datastax.oss.driver.internal.core.`type`.codec.extras.scala

import com.datastax.oss.driver.api.core.`type`.codec.{MappingCodec, TypeCodecs}
import com.datastax.oss.driver.api.core.`type`.reflect.GenericType

class BigDecimalCodec extends MappingCodec[java.math.BigDecimal, BigDecimal](TypeCodecs.DECIMAL, GenericType.of(classOf[BigDecimal])) {

  override def innerToOuter(value: java.math.BigDecimal): BigDecimal = {
    new BigDecimal(value)
  }

  override def outerToInner(value: BigDecimal): java.math.BigDecimal = {
    value.bigDecimal.stripTrailingZeros()
  }
}
