package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.MonthDay;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class MonthDayCodec extends MappingCodec<String, MonthDay> {

    public MonthDayCodec() {
        super(TypeCodecs.TEXT, GenericType.of(MonthDay.class));
    }

    @Nullable
    @Override
    protected MonthDay innerToOuter(@Nullable String value) {
        return value == null ? null : MonthDay.parse(value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable MonthDay value) {
        return value == null ? null : value.toString();
    }
}
