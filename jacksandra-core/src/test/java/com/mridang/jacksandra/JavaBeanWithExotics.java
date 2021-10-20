package com.mridang.testcontainers-minio;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mridang.testcontainers-minio.types.CqlAscii;
import com.mridang.testcontainers-minio.types.CqlBlob;
import com.mridang.testcontainers-minio.types.CqlDuration;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

@SuppressWarnings({"unused", "JavadocReference"})
@CqlName("myjavabeanwithexotics")
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public class JavaBeanWithExotics implements Serializable {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionkey")
    public String myPartitionKey;

    /**
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#ASCII}
     */
    @Nullable
    @CqlName("toascii")
    public CqlAscii toAscii;

    /**
     * {@link com.datastax.dse.driver.internal.core.graph.GraphSON1SerdeTP.DefaultInetAddressDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#INET}
     */
    @Nullable
    @CqlName("toip")
    public InetAddress toIP;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#UUID}
     */
    @Nullable
    @CqlName("touuid")
    public UUID toUUID;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#UUID}
     */
    @Nullable
    @CqlName("totimeuuid")
    public CqlTimeUUID toTimeUUID;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#UUID}
     */
    @Nullable
    @CqlName("toblob")
    public CqlBlob toBlob;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#DURATION}
     */
    @Nullable
    @CqlName("toduration")
    public CqlDuration toDuration;

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
