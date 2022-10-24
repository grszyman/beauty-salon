package pl.szymsoft.salon.values;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

public enum RandomPhoneNumber {
    ;

    private static final RandomGenerator random = new SecureRandom();


    public static PhoneNumber get() {
        return randomPhoneNumber();
    }

    public static PhoneNumber randomPhoneNumber() {
        return PhoneNumber.of(randomPhone());
    }

    @SuppressWarnings({"MagicNumber", "AutoBoxing"})
    private static String randomPhone() {
        return String.format("%03d %03d %04d",
                random.nextInt(999),
                random.nextInt(999),
                random.nextInt(9999)
        );
    }

}
