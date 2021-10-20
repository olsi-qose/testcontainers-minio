package com.mridang.testcontainers-minio.types;

import java.io.Serializable;
import java.util.Objects;

public class CqlAscii implements Serializable {

    private final String string;

    public CqlAscii(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CqlAscii)) return false;

        CqlAscii cqlAscii = (CqlAscii) o;

        return Objects.equals(string, cqlAscii.string);
    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }

    @Override
    public String toString() {
        return string;
    }
}
