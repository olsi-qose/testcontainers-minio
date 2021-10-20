package com.mridang.testcontainers-minio;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@SuppressWarnings({"unused", "CommentedOutCode"})
@CqlName("myjavabeanwithtemporals")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithTemporal implements Serializable {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionkey")
    public String myPartitionKey;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("todatetimestamp")
    public Date toDateTimestamp;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("totimestamptimestamp")
    public Timestamp toTimestampTimestamp;

    /**
     * See com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("toinstanttimestamp")
    public Instant toInstantTimestamp;

    /**
     * Force shaping.
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer}
     */
    @Nullable
    @CqlName("tooffsetdatetimestring")
    public OffsetDateTime toOffsetDateTimeString;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer}
     * {@link com.datastax.oss.driver.internal.core.type.codec.extras.time.ZonedTimestampCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
//    @Nullable
//    @CqlName("tozoneddatetimestring")
//    public ZonedDateTime toZonedDateTimeString;

    /**
     * {@link com.datastax.oss.driver.api.core.data.CqlDuration}
     * {@link com.datastax.oss.driver.internal.core.type.codec.CqlDurationCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#DURATION}
     */
    @Nullable
    @CqlName("tojavadurationduration")
    public Duration toJavaDurationDuration;

    /**
     * Force shaping
     * {@link com.datastax.oss.driver.internal.core.type.codec.extras.time.LocalTimestampCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @SuppressWarnings("JavadocReference")
    @Nullable
    @CqlName("tolocaldatetimetimestamp")
    public LocalDateTime toLocalDateTimeTimeStamp;

    /**
     * Force shaping
     * See com.datastax.oss.driver.internal.core.type.codec.DateCodec
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#DATE}
     */
    @Nullable
    @CqlName("tolocaldatedate")
    public LocalDate toLocalDateDate;

    /**
     * Force shaping
     * See com.datastax.oss.driver.internal.core.type.codec.TimeCodec
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIME
     */
    @Nullable
    @CqlName("tolocaltimetime")
    public LocalTime toLocalTimeTime;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.MonthDayDeserializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.ser.MonthDaySerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("tomonthdaystring")
    public MonthDay toMonthDayString;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer
     */
    @Nullable
    @CqlName("tooffsettimestring")
    public OffsetTime toOffsetTimeString;

    /**
     * {@link com.datastax.dse.driver.internal.core.type.codec.time.DateRangeCodec}
     * {@link com.datastax.dse.driver.api.core.type.DseDataTypes#DATE_RANGE}
     */
    @Nullable
    @CqlName("toperioddaterange")
    public Period toPeriodDateRange;

    /**
     * No shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.YearSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT
     */
    @Nullable
    @CqlName("toyearstring")
    public Year toYearString;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer
     * and {@link com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toyearmonthstring")
    public YearMonth toYearMonthString;

    /**
     * No shaping.
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.ZoneIdSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("tozoneidstring")
    public ZoneId toZoneIdString;

    /**
     * No shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.JSR310StringParsableDeserializer#ZONE_OFFSET}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("tozoneoffsetstring")
    public ZoneOffset toZoneOffsetString;

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
