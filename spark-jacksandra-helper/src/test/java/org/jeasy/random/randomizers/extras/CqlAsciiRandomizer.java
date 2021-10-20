package org.jeasy.random.randomizers.extras;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;

import com.mridang.testcontainers-minio.types.CqlAscii;

public class CqlAsciiRandomizer implements Randomizer<CqlAscii> {

    private final StringRandomizer stringRandomizer;

    public CqlAsciiRandomizer() {
        this(new StringRandomizer());
    }

    public CqlAsciiRandomizer(StringRandomizer stringRandomizer) {
        this.stringRandomizer = stringRandomizer;
    }

    @Override
    public CqlAscii getRandomValue() {
        return new CqlAscii(stringRandomizer.getRandomValue());
    }
}
