package com.mridang.testcontainers-minio.types;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CqlTimeUUID implements Serializable {

    private final UUID uuid;

    public CqlTimeUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CqlTimeUUID)) return false;

        CqlTimeUUID that = (CqlTimeUUID) o;

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getUUID().toString();
    }
}
