package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

public class CqlTimeUUIDSerializer extends StdScalarSerializer<CqlTimeUUID> {

    public CqlTimeUUIDSerializer() {
        super(CqlTimeUUID.class);
    }

    @Override
    public void serialize(CqlTimeUUID bbuf, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(bbuf.toString());
    }
}
