package com.mridang.testcontainers-minio;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mridang.testcontainers-minio.types.FrozenList;
import com.mridang.testcontainers-minio.types.FrozenSet;

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
@CqlName("myjavabeanwithcollections")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithCollections implements Serializable {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionkey")
    public String myPartitionKey;

    @Nullable
    @CqlName("tofrozenliststring")
    public FrozenList<String> toFrozenListString;

    @Nullable
    @CqlName("toliststring")
    public List<String> toListString;

    @Nullable
    @CqlName("toimmutableliststring")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public ImmutableList<String> toImmutableListString;

    @Nullable
    @CqlName("tosetliststring")
    public FrozenSet<String> toSetListString;

    @Nullable
    @CqlName("tosetstring")
    public Set<String> toSetString;

    @Nullable
    @CqlName("toimmutablesetstring")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public ImmutableSet<String> toImmutableSetString;

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
