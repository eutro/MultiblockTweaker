package eutros.multiblocktweaker.helper;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@SuppressWarnings("unused")
public class ReflectionHelper {

    private static Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T, C> T getPrivate(Class<? super C> fieldClass, String fieldName, C object) {
        try {
            return (T) FieldUtils.getField(fieldClass, fieldName, true).get(object);
        } catch(IllegalAccessException | ClassCastException | NullPointerException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't get field %s of %s.", fieldClass, fieldName, object), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                Z object) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName);
            return Optional.ofNullable((T) method.invoke(object));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z, A> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                   Z object,
                                                   Class<A> clazzA,
                                                   A paramA) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName, clazzA);
            return Optional.ofNullable((T) method.invoke(object, paramA));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z, A, B> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                      Z object,
                                                      Class<A> clazzA,
                                                      Class<B> clazzB,
                                                      A paramA,
                                                      B paramB) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName, clazzA, clazzB);
            return Optional.ofNullable((T) method.invoke(object, paramA, paramB));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z, A, B, C> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                         Z object,
                                                         Class<A> clazzA,
                                                         Class<B> clazzB,
                                                         Class<C> clazzC,
                                                         A paramA,
                                                         B paramB,
                                                         C paramC) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName, clazzA, clazzB, clazzC);
            method.setAccessible(true);
            return Optional.ofNullable((T) method.invoke(object, paramA, paramB, paramC));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z, A, B, C, D> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                            Z object,
                                                            Class<A> clazzA,
                                                            Class<B> clazzB,
                                                            Class<C> clazzC,
                                                            Class<D> clazzD,
                                                            A paramA,
                                                            B paramB,
                                                            C paramC,
                                                            D paramD) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName, clazzA, clazzB, clazzC, clazzD);
            method.setAccessible(true);
            return Optional.ofNullable((T) method.invoke(object, paramA, paramB, paramC, paramD));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable // null denotes failure, empty denotes null return.
    public static <T, Z, A, B, C, D, E> Optional<T> callMethod(Class<? super Z> methodClass, String methodName,
                                                               Z object,
                                                               Class<A> clazzA,
                                                               Class<B> clazzB,
                                                               Class<C> clazzC,
                                                               Class<D> clazzD,
                                                               Class<E> clazzE,
                                                               A paramA,
                                                               B paramB,
                                                               C paramC,
                                                               D paramD,
                                                               E paramE) {
        try {
            Method method = methodClass.getDeclaredMethod(methodName, clazzA, clazzB, clazzC, clazzD, clazzE);
            method.setAccessible(true);
            return Optional.ofNullable((T) method.invoke(object, paramA, paramB, paramC, paramD, paramE));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't call method %s of %s.", methodClass, methodName, object));
            //noinspection OptionalAssignedToNull
            return null;
        }
    }

}
