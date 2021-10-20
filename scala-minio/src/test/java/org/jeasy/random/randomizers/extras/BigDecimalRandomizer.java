package org.jeasy.random.randomizers.extras;

import java.math.BigDecimal;

public class BigDecimalRandomizer extends org.jeasy.random.randomizers.number.BigDecimalRandomizer {

    @Override
    public BigDecimal getRandomValue() {
        return super.getRandomValue().stripTrailingZeros();
    }
}
