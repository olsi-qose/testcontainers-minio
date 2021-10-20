package com.datastax.oss.driver.internal.core.type.codec.extras;

import com.datastax.oss.driver.internal.core.type.codec.extras.time.CqlDurationCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.DurationTypeCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.LegacyDateCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.LegacyTimestampCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.LocalDateCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.LocalDateTimeCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.MonthDayCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.YearCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.YearMonthCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.ZoneIdCodec;
import com.datastax.oss.driver.internal.core.type.codec.extras.time.ZoneOffsetCodec;
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry;

public class CassandraCodecRegistry extends DefaultCodecRegistry {

    public CassandraCodecRegistry() {
        super("Jacksandra");
        register(new YearMonthCodec());
        register(new MonthDayCodec());
        register(new YearCodec());
        register(new ZoneIdCodec());
        register(new ZoneOffsetCodec());
        register(new LocalDateCodec());
        register(new LegacyTimestampCodec());
        register(new LocalDateTimeCodec());
        register(new DurationTypeCodec());
        register(new CqlBlobCodec());
        register(new CqlAsciiCodec());
        register(new CqlTimeUUIDCodec());
        register(new CqlDurationCodec());
        register(new LegacyDateCodec());
    }
}
