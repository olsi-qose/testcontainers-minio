package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.mridang.testcontainers-minio.types.CqlDuration;

public class CqlDurationDeserializer extends StdScalarDeserializer<CqlDuration> {

    public CqlDurationDeserializer() {
        super(CqlDuration.class);
    }

    @Override
    public CqlDuration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return CqlDuration.from(p.getValueAsString());
    }
}
