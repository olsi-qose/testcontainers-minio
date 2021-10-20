package com.mridang.testcontainers-minio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * tinyint	integers	1 byte integer
 * smallint	integers	2 byte integer
 * int	integers	32-bit signed integer
 * bigint	integers	64-bit signed long
 * varint	integers	Arbitrary-precision integer
 * <p>
 * float	integers, floats	32-bit IEEE-754 floating point
 * double	integers, floats	64-bit IEEE-754 floating point
 * decimal	integers, floats	Variable-precision decimal
 */
@SuppressWarnings("unused")
@CqlName("myjavabeanwithnumbers")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithNumbers implements Serializable {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionkey")
    public String myPartitionKey;

    @Nullable
    @CqlName("totinyint")
    public Byte toTinyInt;

    @Nullable
    @CqlName("tosmallint")
    public Short toSmallInt;

    @Nullable
    @CqlName("toint")
    public Integer toInt;

    @Nullable
    @CqlName("tobigint")
    public Long toBigInt;

    @Nullable
    @CqlName("tovarint")
    public BigInteger toVarInt;

    @Nullable
    @CqlName("tofloat")
    public Float toFloat;

    @Nullable
    @CqlName("todouble")
    public Double toDouble;

    @Nullable
    @CqlName("tobigdecimal")
    public BigDecimal toBigDecimal;

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
