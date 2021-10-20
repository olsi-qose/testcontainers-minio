package com.mridang.testcontainers-minio.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;

import org.junit.Test;

public class FrozenSetTest {

    @Test
    public void testEqualityAgainstFrozenSet() {
        assertEquals(Collections.singleton("moo"), new FrozenSet<>(Collections.singleton("moo")));
    }

    @Test
    public void testFrozenSetEqualityAgainst() {
        assertEquals(new FrozenSet<>(Collections.singleton("moo")), Collections.singleton("moo"));
    }

    @Test
    public void testFrozenSetEqualityAgainstNull() {
        assertNotEquals(new FrozenSet<>(Collections.singleton("moo")), null);
    }

    @Test
    public void testNullEqualityAgainstFrozenSet() {
        assertNotEquals(null, new FrozenSet<>(Collections.singleton("moo")));
    }
}
