package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.util.UUID;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

import edu.umd.cs.findbugs.annotations.Nullable;

public class CqlTimeUUIDCodec extends MappingCodec<UUID, CqlTimeUUID> {

    public CqlTimeUUIDCodec() {
        super(TypeCodecs.TIMEUUID, GenericType.of(CqlTimeUUID.class));
    }

    @Nullable
    @Override
    protected CqlTimeUUID innerToOuter(@Nullable UUID value) {
        return value == null ? null : new CqlTimeUUID(value);
    }

    @Nullable
    @Override
    protected UUID outerToInner(@Nullable CqlTimeUUID value) {
        return value == null ? null : value.getUUID();
    }
}
