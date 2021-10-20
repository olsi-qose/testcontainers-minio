package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.mridang.testcontainers-minio.types.CqlBlob;

public class CqlBlobSerializer extends StdScalarSerializer<CqlBlob> {

    public CqlBlobSerializer() {
        super(CqlBlob.class);
    }

    @Override
    public void serialize(CqlBlob bbuf, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(bbuf.toString());
    }
}
