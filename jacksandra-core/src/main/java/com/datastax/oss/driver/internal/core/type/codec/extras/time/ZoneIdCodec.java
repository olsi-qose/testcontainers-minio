package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.ZoneId;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ZoneIdCodec extends MappingCodec<String, ZoneId> {

    public ZoneIdCodec() {
        super(TypeCodecs.TEXT, GenericType.of(ZoneId.class));
    }

    @Nullable
    @Override
    protected ZoneId innerToOuter(@Nullable String value) {
        return value == null ? null : ZoneId.of(value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable ZoneId value) {
        return value == null ? null : value.toString();
    }
}
