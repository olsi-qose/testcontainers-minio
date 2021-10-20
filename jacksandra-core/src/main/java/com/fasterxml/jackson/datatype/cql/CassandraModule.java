package com.fasterxml.jackson.datatype.cql;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.cql.deser.CqlAsciiDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.CqlBlobDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.CqlDurationDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.CqlTimeUUIDDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.DateDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.TimestampDeserializer;
import com.fasterxml.jackson.datatype.cql.ser.CqlAsciiSerializer;
import com.fasterxml.jackson.datatype.cql.ser.CqlBlobSerializer;
import com.fasterxml.jackson.datatype.cql.ser.CqlDurationSerializer;
import com.fasterxml.jackson.datatype.cql.ser.CqlTimeUUIDSerializer;
import com.fasterxml.jackson.datatype.cql.ser.DateAsMillisSerializer;
import com.fasterxml.jackson.datatype.cql.ser.InstantAsMillisSerializer;
import com.fasterxml.jackson.datatype.cql.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jdk8.PackageVersion;
import com.mridang.testcontainers-minio.types.CqlAscii;
import com.mridang.testcontainers-minio.types.CqlBlob;
import com.mridang.testcontainers-minio.types.CqlDuration;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

public class CassandraModule extends SimpleModule {

    public CassandraModule() {
        super(PackageVersion.VERSION);
        addDeserializer(Date.class, new DateDeserializer());
        addDeserializer(Duration.class, new DurationDeserializer());
        addDeserializer(Instant.class, new InstantDeserializer());
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        addDeserializer(Timestamp.class, new TimestampDeserializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        addSerializer(CqlBlob.class, new CqlBlobSerializer());
        addDeserializer(CqlBlob.class, new CqlBlobDeserializer());
        addSerializer(CqlDuration.class, new CqlDurationSerializer());
        addDeserializer(CqlDuration.class, new CqlDurationDeserializer());
        addSerializer(CqlAscii.class, new CqlAsciiSerializer());
        addDeserializer(CqlAscii.class, new CqlAsciiDeserializer());
        addSerializer(CqlTimeUUID.class, new CqlTimeUUIDSerializer());
        addDeserializer(CqlTimeUUID.class, new CqlTimeUUIDDeserializer());
        addSerializer(Instant.class, new InstantAsMillisSerializer());
        addSerializer(Date.class, new DateAsMillisSerializer());
    }

    @Override
    public String getModuleName() {
        return "CassandraModule";
    }
}
