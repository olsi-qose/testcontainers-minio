package org.jeasy.random.randomizers.extras;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InstantRandomizer extends org.jeasy.random.randomizers.time.InstantRandomizer {

    @Override
    public Instant getRandomValue() {
        return super.getRandomValue().truncatedTo(ChronoUnit.MILLIS);
    }
}
