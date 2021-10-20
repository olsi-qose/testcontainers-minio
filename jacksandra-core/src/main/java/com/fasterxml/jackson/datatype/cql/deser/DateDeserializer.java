package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.datastax.oss.driver.internal.core.type.codec.TimestampCodec;
import com.datastax.oss.driver.internal.core.util.Strings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class DateDeserializer extends StdScalarDeserializer<Date> {

    public static final TimestampCodec TIMESTAMP_CODEC = new TimestampCodec();

    public DateDeserializer() {
        super(Instant.class);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Optional.ofNullable(TIMESTAMP_CODEC.parse(Strings.quote(jsonParser.getValueAsString())))
                .map(Date::from)
                .orElse(null);
    }
}
