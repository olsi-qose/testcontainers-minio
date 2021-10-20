package com.mridang.testcontainers-minio;

import java.io.Serializable;
import java.util.Map;

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
import com.google.common.collect.ImmutableMap;
import com.mridang.testcontainers-minio.JavaBeanWithUDT.SomeUDT;

@SuppressWarnings("unused")
@CqlName("myjavabeanwithmaps")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithMaps implements Serializable {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionkey")
    public String myPartitionKey;

//    @Nullable
//    @CqlName("tofrozenliststring")
//    public FrozenList<String> toFrozenListString;

    @Nullable
    @CqlName("tostringfloatmap")
    public Map<String, Float> toStringFloatMap;

    @Nullable
    @CqlName("toimmutablestringdoublemap")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public ImmutableMap<String, Double> toImmutableStringDoubleMap;

    @Nullable
    @CqlName("tomapstringkeyfrozenudtval")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public Map<String, SomeUDT> toMapStringKeyFrozenUdtVal;

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
