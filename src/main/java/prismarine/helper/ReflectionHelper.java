package prismarine.helper;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.util.Map;

public final class ReflectionHelper {

    private static final Map<Class<?>, Constructor<?>> constructorMap = Maps.newHashMap();

    private ReflectionHelper() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> findConstructor(Class<T> clazz) {
        Constructor<T> constructor = (Constructor<T>) constructorMap.get(clazz);
        if (constructor == null) {
            try {
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw Throwables.propagate(e);
            }
            constructor.setAccessible(true);
            constructorMap.put(clazz, constructor);
        }
        return constructor;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return findConstructor(clazz).newInstance();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
