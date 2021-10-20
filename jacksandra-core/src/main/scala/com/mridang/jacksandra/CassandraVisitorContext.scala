package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.module.jsonSchema.factories.VisitorContext
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema

/**
 * Custom visitor context that is used for collecting all user-defined types
 * encountered while traversing the bean properties.
 *
 * As user-defined types need to be created prior to the actual table
 * generation - the list of user-defined type dependencies can simply be
 * looked up from here.
 *
 * @author mridang
 */
class CassandraVisitorContext extends VisitorContext {

  var seenObjects: Boolean = false

  var udts: Map[CqlName, ObjectSchema] = Map.empty

  def addSeenUDT(name: CqlName, schema: ObjectSchema): Unit = {
    udts += (name -> schema)
  }

}
