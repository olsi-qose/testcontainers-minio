/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.testcontainers-minio.types.CqlDuration;

public class CqlDurationCodec extends MappingCodec<com.datastax.oss.driver.api.core.data.CqlDuration, CqlDuration> {

    public CqlDurationCodec() {
        super(TypeCodecs.DURATION, GenericType.of(CqlDuration.class));
    }


    @Nullable
    @Override
    protected CqlDuration innerToOuter(@Nullable com.datastax.oss.driver.api.core.data.CqlDuration value) {
        return value == null ? null : CqlDuration.from(value.toString());
    }

    @Nullable
    @Override
    protected com.datastax.oss.driver.api.core.data.CqlDuration outerToInner(@Nullable CqlDuration value) {
        return value == null ? null : com.datastax.oss.driver.api.core.data.CqlDuration.from(value.toString());
    }
}
