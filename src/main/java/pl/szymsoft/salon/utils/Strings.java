package pl.szymsoft.salon.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public enum Strings {
    ;

    public static String requireNonBlank(String text, String name) {
        return Objects.require(text, StringUtils::isNotBlank, name + " must not be blank");
    }

    public static String requireNonBlankElse(String text, String defaultText) {
        return Objects.requireElse(text, StringUtils::isNotBlank, defaultText);
    }

    public static String requireNonBlankElseGet(String text, Supplier<String> supplier) {
        return Objects.requireElseGet(text, StringUtils::isNotBlank, supplier);
    }
}
