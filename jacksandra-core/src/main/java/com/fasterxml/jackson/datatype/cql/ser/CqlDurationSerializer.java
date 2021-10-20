package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.mridang.testcontainers-minio.types.CqlDuration;

public class CqlDurationSerializer extends StdScalarSerializer<CqlDuration> {

    public CqlDurationSerializer() {
        super(CqlDuration.class);
    }

    @Override
    public void serialize(CqlDuration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toString());
    }
}
