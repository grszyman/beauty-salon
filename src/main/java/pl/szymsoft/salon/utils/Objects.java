package pl.szymsoft.salon.utils;

import org.springframework.lang.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public enum Objects {
    ;

    public static <T> T require(@Nullable T object, Predicate<? super T> predicate, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        if (predicate.test(object)) {
            return object;
        }
        throw new IllegalArgumentException(message);
    }

    public static <T> T requireElse(@Nullable T object, Predicate<? super T> predicate, T defaultObject) {
        if (object != null && predicate.test(object)) {
            return object;
        }
        return defaultObject;
    }

    public static <T> T requireElseGet(@Nullable T object, Predicate<? super T> predicate, Supplier<? extends T> supplier) {
        if (object != null && predicate.test(object)) {
            return object;
        }
        return supplier.get();
    }
}
