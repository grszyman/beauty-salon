package pl.szymsoft.salon.utils;

@SuppressWarnings("BooleanParameter")
public enum Ensure {
    ;

    public static void ensureThat(boolean satisfied, String message) {
        if (!satisfied) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void ensureThat(boolean satisfied, String message, Object... args) {
        if (!satisfied) {
            throw new IllegalArgumentException(message.formatted(args));
        }
    }
}
