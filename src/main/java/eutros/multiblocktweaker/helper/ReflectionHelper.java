package eutros.multiblocktweaker.helper;

import com.google.common.base.Preconditions;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.CraftTweaker;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

public class ReflectionHelper {

    private static Map<Class<?>, Map<String, MethodHandle>> handles = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T, C> T getPrivate(Class<? super C> fieldClass, String fieldName, C object) throws ClassCastException {
        try {
            return (T) handles
                    .computeIfAbsent(fieldClass, c -> new HashMap<>())
                    .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                    .invoke(object);
        } catch (Throwable e) {
            CraftTweakerAPI.logError(String.format("No field \"%s\" found", fieldName));
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, C> T getStatic(Class<? super C> fieldClass, String fieldName) throws ClassCastException {
        try {
            return (T) handles
                    .computeIfAbsent(fieldClass, c -> new HashMap<>())
                    .computeIfAbsent(fieldName, computeHandle(fieldClass, fieldName))
                    .invoke(fieldClass);
        } catch (Throwable e) {
            CraftTweakerAPI.logError(String.format("No static field \"%s\" found", fieldName));
            return null;
        }
    }

    private static <C> Function<String, MethodHandle> computeHandle(Class<? super C> fieldClass, String fieldName) {
        return p -> {
            try {
                return MethodHandles.publicLookup()
                        .unreflectGetter(
                                Preconditions.checkNotNull(
                                        FieldUtils.getField(
                                                fieldClass,
                                                fieldName,
                                                true
                                        ),
                                        "Couldn't find field %s of %s.",
                                        fieldName, fieldClass
                                )
                        );
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("Couldn't access field %s of %s", fieldName, fieldClass), e);
            }
        };
    }

}
