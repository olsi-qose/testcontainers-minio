package org.jeasy.random.randomizers.extras;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.number.ByteRandomizer;
import org.jeasy.random.randomizers.number.ShortRandomizer;

public class ByteBufferRandomizer implements Randomizer<ByteBuffer> {

    private final ByteRandomizer delegate;

    /**
     * Create a new {@link ByteBufferRandomizer}.
     */
    public ByteBufferRandomizer() {
        delegate = new ByteRandomizer();
    }

    /**
     * Create a new {@link ByteBufferRandomizer}.
     *
     * @param seed initial seed
     */
    public ByteBufferRandomizer(final long seed) {
        delegate = new ByteRandomizer(seed);
    }

    @Override
    public ByteBuffer getRandomValue() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Math.abs(new ShortRandomizer().getRandomValue()));
        IntStream.range(0, byteBuffer.capacity())
                .forEach(value -> byteBuffer.put(delegate.getRandomValue()));
        return byteBuffer;
    }
}
