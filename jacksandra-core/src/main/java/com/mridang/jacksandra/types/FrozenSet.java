package com.mridang.testcontainers-minio.types;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;

/**
 * Wrapped set implementation used during schema introspection to deduce if the set
 * is frozen.
 * <p>
 * The name may sound misleading as this acts a marker and does not provide any
 * immutability guarantees.
 *
 * @author mridang
 */
@SuppressWarnings("NullableProblems")
public class FrozenSet<E> implements Set<E>, Frozen, Serializable {

    private final Set<E> backingSet;

    @SuppressWarnings("unused")
    public FrozenSet() {
        this(new HashSet<>());
    }

    public FrozenSet(Set<E> backingSet) {
        this.backingSet = backingSet;
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    @Override
    public boolean isEmpty() {
        return backingSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backingSet.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return backingSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return backingSet.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(T[] a) {
        return backingSet.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return backingSet.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return backingSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return backingSet.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backingSet.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backingSet.removeAll(c);
    }

    @Override
    public void clear() {
        backingSet.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof AbstractSet) {
            return o.equals(this);
        }

        if (!(o instanceof FrozenSet)) return false;

        FrozenSet<?> frozenSet = (FrozenSet<?>) o;

        return Objects.equals(backingSet, frozenSet.backingSet);
    }

    @Override
    public int hashCode() {
        return backingSet != null ? backingSet.hashCode() : 0;
    }

    @Override
    public Spliterator<E> spliterator() {
        return backingSet.spliterator();
    }
}
