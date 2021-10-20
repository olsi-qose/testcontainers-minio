package org.jeasy.random.randomizers.extras;

import org.jeasy.random.api.Randomizer;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.mridang.testcontainers-minio.types.CqlTimeUUID;

public class CqTimeUUIDRandomizer implements Randomizer<CqlTimeUUID> {

    private final InstantRandomizer instantRandomizer;

    public CqTimeUUIDRandomizer() {
        this(new InstantRandomizer());
    }

    public CqTimeUUIDRandomizer(InstantRandomizer instantRandomizer) {
        this.instantRandomizer = instantRandomizer;
    }

    @Override
    public CqlTimeUUID getRandomValue() {
        return new CqlTimeUUID(Uuids.endOf(instantRandomizer.getRandomValue().getEpochSecond()));
    }
}
