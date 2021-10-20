package com.datastax.oss.driver.internal.core.`type`.codec.extras.scala

import com.datastax.oss.driver.internal.core.`type`.codec.extras.CassandraCodecRegistry

class ScalaCodecRegistry extends CassandraCodecRegistry {

  register(new BigDecimalCodec)
  register(new BigIntCodec)
}
