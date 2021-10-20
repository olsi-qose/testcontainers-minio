package com.mridang.testcontainers-minio.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Annotates the field or getter of an {@link Entity} property, to indicate that it's a clustering
 * column and sets the clustering order.
 *
 * <p>Example:
 *
 * <pre>
 * &#64;OrderedClusteringColumn(isAscending = false, value = 0)
 * </pre>
 * <p>
 * This information is used by the mapper processor to generate default queries (for example a basic
 * {@link Select}).
 *
 * <p>If there are multiple clustering columns, you must specify {@link #value()} to indicate the
 * position of each property:
 *
 * <pre>
 * &#64;OrderedClusteringColumn(isAscending = false, value = 1) private int month;
 * &#64;OrderedClusteringColumn(isAscending = false, value = 2) private int day;
 * </pre>
 * <p>
 * If you don't specify positions, or if there are duplicates, the mapper processor will issue a
 * compile-time error.
 *
 * <p>This annotation is mutually exclusive with {@link PartitionKey}.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderedClusteringColumn {

    boolean isAscending();

    /**
     * The position of the clustering column.
     *
     * <p>This is only required if there are multiple clustering columns. Positions are not strictly
     * required to be consecutive or start at a given index, but for clarity it is recommended to use
     * consecutive integers.
     */
    int value() default 0;
}
