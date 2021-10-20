package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;
import java.time.Instant;

import com.datastax.oss.driver.internal.core.type.codec.TimestampCodec;
import com.datastax.oss.driver.internal.core.util.Strings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class InstantDeserializer extends StdScalarDeserializer<Instant> {

    public static final TimestampCodec TIMESTAMP_CODEC = new TimestampCodec();

    public InstantDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TIMESTAMP_CODEC.parse(Strings.quote(jsonParser.getValueAsString()));
    }
}
