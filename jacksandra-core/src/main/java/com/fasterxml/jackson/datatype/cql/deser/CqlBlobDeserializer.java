package com.fasterxml.jackson.datatype.cql.deser;

import java.io.IOException;

import com.datastax.oss.protocol.internal.util.Bytes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.mridang.testcontainers-minio.types.CqlBlob;

public class CqlBlobDeserializer extends StdScalarDeserializer<CqlBlob> {

    public CqlBlobDeserializer() {
        super(CqlBlob.class);
    }

    @Override
    public CqlBlob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new CqlBlob(Bytes.fromHexString(p.getValueAsString()).array());
    }
}
