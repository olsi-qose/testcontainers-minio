package com.mridang.testcontainers-minio.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collections;

import org.junit.Test;

public class FrozenListTest {

    @Test
    public void testEqualityAgainstFrozenList() {
        assertEquals(Collections.singletonList("moo"), new FrozenList<>(Collections.singletonList("moo")));
    }

    @Test
    public void testFrozenListEqualityAgainst() {
        assertEquals(new FrozenList<>(Collections.singletonList("moo")), Collections.singletonList("moo"));
    }

    @Test
    public void testFrozenListEqualityAgainstNull() {
        assertNotEquals(new FrozenList<>(Collections.singletonList("moo")), null);
    }

    @Test
    public void testNullEqualityAgainstFrozenList() {
        assertNotEquals(null, new FrozenList<>(Collections.singletonList("moo")));
    }
}
