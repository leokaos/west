package org.west.utilitarios;

import java.util.Collection;

public class ValidadorUtil {

    public static boolean isNotNull(Object obj) {
        return not(isNull(obj));
    }

    public static boolean not(boolean condicional) {
        return !condicional;
    }

    public static boolean isNull(Object obj) {
        return (obj == null);
    }

    private static boolean isEmpty(Collection<?> collection) {
        if (isNotNull(collection)) {
            return collection.isEmpty();
        }

        return true;
    }

    private static boolean isNotEmpty(Collection<?> collection) {
        return not(isEmpty(collection));
    }

    private static boolean isEmpty(Number number) {
        if (isNotNull(number)) {
            return number.doubleValue() == 0.0;
        }

        return true;
    }

    private static boolean isNotEmpty(Number number) {
        return not(isEmpty(number));
    }

    private static boolean isEmpty(String string) {
        if (isNotNull(string)) {
            return string.isEmpty();
        }

        return true;
    }

    private static boolean isNotEmpty(String string) {
        return not(isEmpty(string));
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof Collection) {
            return isEmpty((Collection) obj);
        }

        if (obj instanceof String) {
            return isEmpty((String) obj);
        }

        if (obj instanceof Number) {
            return isEmpty((Number) obj);
        }

        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        if (isNull(obj)) {
            return false;
        }

        return not(isEmpty(obj));
    }
}