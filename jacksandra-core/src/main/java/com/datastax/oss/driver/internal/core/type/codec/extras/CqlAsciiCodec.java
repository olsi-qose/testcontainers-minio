package com.datastax.oss.driver.internal.core.type.codec.extras;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.testcontainers-minio.types.CqlAscii;

import edu.umd.cs.findbugs.annotations.Nullable;

public class CqlAsciiCodec extends MappingCodec<String, CqlAscii> {

    public CqlAsciiCodec() {
        super(TypeCodecs.ASCII, GenericType.of(CqlAscii.class));
    }

    @Nullable
    @Override
    protected CqlAscii innerToOuter(@Nullable String value) {
        return value == null ? null : new CqlAscii(value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable CqlAscii value) {
        return value == null ? null : value.getString();
    }
}
