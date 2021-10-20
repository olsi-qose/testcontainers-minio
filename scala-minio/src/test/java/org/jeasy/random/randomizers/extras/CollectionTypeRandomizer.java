package org.jeasy.random.randomizers.extras;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.ContextAwareRandomizer;
import org.jeasy.random.api.RandomizerContext;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.util.ReflectionUtils;

public abstract class CollectionTypeRandomizer<T> implements ContextAwareRandomizer<T> {

    private RandomizerContext context;

    @Override
    public void setRandomizerContext(RandomizerContext context) {
        this.context = context;
    }

    public abstract T getRandomValue(Object[] value);

    @Override
    public T getRandomValue() {
        try {
            return getRandomValue(getRandomCollection(context.getTargetType().getField(context.getCurrentField())));
        } catch (NoSuchFieldException e) {
            try {
                return getRandomValue(getRandomCollection(context.getTargetType().getDeclaredField(context.getCurrentField())));
            } catch (NoSuchFieldException noSuchFieldException) {
                throw new RuntimeException(noSuchFieldException);
            }
        }
    }

    Object[] getRandomCollection(final Field field) {
        EasyRandom easyRandom = new EasyRandom(context.getParameters());
        EasyRandomParameters.Range<Integer> collectionSizeRange = context.getParameters().getCollectionSizeRange();
        int randomSize = new IntegerRangeRandomizer(collectionSizeRange.getMin(), collectionSizeRange.getMax(), easyRandom.nextLong()).getRandomValue();
        Type fieldGenericType = field.getGenericType();
        Object[] collection = new Object[randomSize];
        if (ReflectionUtils.isParameterizedType(fieldGenericType)) { // populate only parametrized types, raw types will be empty
            ParameterizedType parameterizedType = (ParameterizedType) fieldGenericType;
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (ReflectionUtils.isPopulatable(type)) {
                for (int i = 0; i < randomSize; i++) {
                    collection[i] = easyRandom.nextObject((Class<?>) type);
                }

            }
        }
        return collection;
    }
}
