package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.mridang.testcontainers-minio.types.CqlAscii;

public class CqlAsciiSerializer extends StdScalarSerializer<CqlAscii> {

    public CqlAsciiSerializer() {
        super(CqlAscii.class);
    }

    @Override
    public void serialize(CqlAscii bbuf, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(bbuf.toString());
    }
}
