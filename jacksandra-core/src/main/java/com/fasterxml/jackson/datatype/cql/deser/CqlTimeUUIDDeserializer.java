package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

public class CqlTimeUUIDDeserializer extends StdScalarDeserializer<CqlTimeUUID> {

    public CqlTimeUUIDDeserializer() {
        super(CqlTimeUUID.class);
    }

    @Override
    public CqlTimeUUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new CqlTimeUUID(UUID.fromString(p.getValueAsString()));
    }
}
