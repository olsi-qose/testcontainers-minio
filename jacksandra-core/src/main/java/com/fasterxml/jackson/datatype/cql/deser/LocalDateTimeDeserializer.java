package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import com.datastax.oss.driver.internal.core.type.codec.TimestampCodec;
import com.datastax.oss.driver.internal.core.util.Strings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class LocalDateTimeDeserializer extends StdScalarDeserializer<LocalDateTime> {

    public static final TimestampCodec TIMESTAMP_CODEC = new TimestampCodec();

    public LocalDateTimeDeserializer() {
        super(Timestamp.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        return Optional.ofNullable(TIMESTAMP_CODEC.parse(Strings.quote(jsonParser.getValueAsString())))
                .map(temporal -> LocalDateTime.ofInstant(temporal, ZoneOffset.UTC))
                .orElse(null);
    }
}
