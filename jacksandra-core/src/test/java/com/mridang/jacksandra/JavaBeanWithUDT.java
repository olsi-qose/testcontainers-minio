package com.mridang.testcontainers-minio;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mridang.testcontainers-minio.types.FrozenList;
import com.mridang.testcontainers-minio.types.FrozenSet;

@SuppressWarnings("unused")
@Entity(defaultKeyspace = "mykeyspace")
@CqlName("myjavabeanwithudt")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithUDT implements Serializable {

    @Nullable
    @PartitionKey
    @CqlName("mypartitionkey")
    public String myPartitionKey;

    @Nullable
    @CqlName("toudt")
    public SomeUDT toUdt;

    @Nullable
    @CqlName("tofrozenudtlist")
    public FrozenList<SomeUDT> toFrozenUdtList;

    @Nullable
    @CqlName("tofrozenudtset")
    public FrozenSet<SomeUDT> toFrozenUdtSet;

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

    @CqlName("myudt")
    @JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
    public static class SomeUDT implements Serializable {

        @Nullable
        @CqlName("somestring")
        public String someString;

        @Nullable
        @CqlName("somedouble")
        public Double someDouble;

        @Nullable
        @CqlName("somelonglist")
        public FrozenList<Long> someLongList;

        @Nullable
        @CqlName("somedoubleset")
        public FrozenSet<Double> someDoubleSet;

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
}
