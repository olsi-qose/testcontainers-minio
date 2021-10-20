package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DurationDeserializer extends StdDeserializer<Duration> {

    public DurationDeserializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        return Duration.parse("PT" + jsonParser.getText());
    }
}
