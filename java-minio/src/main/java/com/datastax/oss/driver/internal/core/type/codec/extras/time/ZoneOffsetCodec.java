package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.ZoneOffset;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ZoneOffsetCodec extends MappingCodec<String, ZoneOffset> {

    public ZoneOffsetCodec() {
        super(TypeCodecs.TEXT, GenericType.of(ZoneOffset.class));
    }

    @Nullable
    @Override
    protected ZoneOffset innerToOuter(@Nullable String value) {
        return value == null ? null : ZoneOffset.of(value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable ZoneOffset value) {
        return value == null ? null : value.toString();
    }
}
