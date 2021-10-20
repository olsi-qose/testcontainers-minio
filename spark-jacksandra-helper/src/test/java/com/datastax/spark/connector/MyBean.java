package com.datastax.spark.connector;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@CqlName("mybean")
public class MyBean implements Serializable {

    @PartitionKey
    @CqlName("some_string")
    public final String someString;

    @CqlName("some_date")
    public final LocalDate someDate;

    @CqlName("some_double")
    public final Double someDouble;

    @CqlName("float_list")
    public final List<Float> floatList;

    @JsonCreator
    public MyBean(String someString, LocalDate someDate, Double someDouble, List<Float> floatList) {
        this.someString = someString;
        this.someDate = someDate;
        this.someDouble = someDouble;
        this.floatList = floatList;
    }

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
