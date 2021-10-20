package com.mridang.testcontainers-minio

import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder}

import java.sql.Timestamp
import java.time._
import java.util.Date

@CqlName("myjavabeanwithtemporals")
@JsonNaming(classOf[PropertyNamingStrategy.LowerCaseStrategy])
class ClassWithTemporal
(
  @PartitionKey
  @CqlName("mypartitionkey")
  var myPartitionKey: String,

  @CqlName("todatetimestamp")
  var toDateTimestamp: Date,

  @CqlName("totimestamptimestamp")
  var toTimestampTimestamp: Timestamp,

  @CqlName("toinstanttimestamp")
  var toInstantTimestamp: Instant,

  @CqlName("tooffsetdatetimestring")
  var toOffsetDateTimeString: OffsetDateTime,

  @CqlName("tojavadurationduration")
  var toJavaDurationDuration: Duration,

  @CqlName("tolocaldatetimetimestamp")
  var toLocalDateTimeTimeStamp: LocalDateTime,

  @CqlName("tolocaldatedate")
  var toLocalDateDate: LocalDate,

  @CqlName("tolocaltimetime")
  var toLocalTimeTime: LocalTime,

  @CqlName("tomonthdaystring")
  var toMonthDayString: MonthDay,

  @CqlName("tooffsettimestring")
  var toOffsetTimeString: OffsetTime,

  @CqlName("toperioddaterange")
  var toPeriodDateRange: Period,

  @CqlName("toyearstring")
  var toYearString: Year,

  @CqlName("toyearmonthstring")
  var toYearMonthString: YearMonth,

  @CqlName("tozoneidstring")
  var toZoneIdString: ZoneId,

  @CqlName("tozoneoffsetstring")
  var toZoneOffsetString: ZoneOffset,

) extends Serializable {

  override def hashCode: Int = {
    HashCodeBuilder.reflectionHashCode(this)
  }

  override def equals(obj: Any): Boolean = {
    EqualsBuilder.reflectionEquals(this, obj)
  }

  override def toString: String = {
    ToStringBuilder.reflectionToString(this)
  }
}

