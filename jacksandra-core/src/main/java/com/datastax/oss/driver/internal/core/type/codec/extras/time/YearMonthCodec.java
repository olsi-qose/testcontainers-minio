package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.YearMonth;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class YearMonthCodec extends MappingCodec<String, YearMonth> {

    public YearMonthCodec() {
        super(TypeCodecs.TEXT, GenericType.of(YearMonth.class));
    }

    @Nullable
    @Override
    protected YearMonth innerToOuter(@Nullable String value) {
        return value == null ? null : YearMonth.parse(value);
    }

    @Nullable
    @Override
    protected String outerToInner(@Nullable YearMonth value) {
        return value == null ? null : value.toString();
    }
}
