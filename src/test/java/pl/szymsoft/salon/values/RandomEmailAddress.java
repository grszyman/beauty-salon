package pl.szymsoft.salon.values;

import static net.andreinc.mockneat.unit.user.Emails.emails;

public enum RandomEmailAddress {
    ;

    public static EmailAddress get() {
        return randomEmailAddress();
    }

    public static EmailAddress randomEmailAddress() {
        return EmailAddress.of(emails().get());
    }
}
