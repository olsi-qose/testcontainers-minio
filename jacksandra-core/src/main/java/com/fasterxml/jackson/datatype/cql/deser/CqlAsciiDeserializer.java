package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.mridang.testcontainers-minio.types.CqlAscii;

public class CqlAsciiDeserializer extends StdScalarDeserializer<CqlAscii> {

    public CqlAsciiDeserializer() {
        super(CqlAscii.class);
    }

    @Override
    public CqlAscii deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new CqlAscii(p.getValueAsString());
    }
}
