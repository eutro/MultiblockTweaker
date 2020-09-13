package eutros.multiblocktweaker.helper;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ReflectionHelper {

    private static Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T, C> T getPrivate(Class<? super C> fieldClass, String fieldName, C object) throws ClassCastException {
        try {
            return (T) FieldUtils.getField(fieldClass, fieldName, true).get(object);
        } catch(IllegalAccessException | NullPointerException e) {
            LOGGER.debug(String.format("Reflection on class %s failed. Couldn't get field %s of %s.", fieldClass, fieldName, object), e);
            return null;
        }
    }

}
