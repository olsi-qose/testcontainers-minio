package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.Duration;

import com.datastax.oss.driver.api.core.data.CqlDuration;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class DurationTypeCodec extends MappingCodec<CqlDuration, Duration> {

    public DurationTypeCodec() {
        super(TypeCodecs.DURATION, GenericType.of(Duration.class));
    }

    @Nullable
    @Override
    protected Duration innerToOuter(@Nullable CqlDuration value) {
        return value == null ? null : Duration.parse(value.toString());
    }

    @Nullable
    @Override
    protected CqlDuration outerToInner(@Nullable Duration value) {
        return value == null ? null : CqlDuration.from(value.toString());
    }
}
