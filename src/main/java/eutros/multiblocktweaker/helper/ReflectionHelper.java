package eutros.multiblocktweaker.helper;

import crafttweaker.CraftTweakerAPI;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ReflectionHelper {

    private static final Map<Class<?>, Map<String, Optional<Field>>> handles = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T, C> T getPrivate(Class<? super C> fieldClass, String fieldName, C object) throws ClassCastException {
        return (T) handles
                .computeIfAbsent(fieldClass, c -> new HashMap<>())
                .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                .map(f-> {
                    try {
                        return f.get(object);
                    } catch (IllegalAccessException e) {
                        CraftTweakerAPI.logError(String.format("No static field \"%s\" found", fieldName));
                    }
                    return null;
                }).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T, C> T getStatic(Class<? super C> fieldClass, String fieldName) throws ClassCastException {
        return (T) handles
                .computeIfAbsent(fieldClass, c -> new HashMap<>())
                .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                .map(f-> {
                    try {
                        return f.get(fieldClass);
                    } catch (IllegalAccessException e) {
                        CraftTweakerAPI.logError(String.format("No static field \"%s\" found", fieldName));
                    }
                    return null;
                }).orElse(null);
    }

    private static <C> Function<String, Optional<Field>> computeHandle(Class<? super C> fieldClass, String fieldName) {
        return p -> {
            try {
                Field field = fieldClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return Optional.of(field);
            } catch (NoSuchFieldException e) {
                CraftTweakerAPI.logError(String.format("Couldn't access field %s of %s", fieldName, fieldClass), e);
                return Optional.empty();
            }
        };
    }

}
