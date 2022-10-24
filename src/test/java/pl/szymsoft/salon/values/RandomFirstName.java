package pl.szymsoft.salon.values;

import static net.andreinc.mockneat.unit.user.Names.names;

public enum RandomFirstName {
    ;

    public static FirstName get() {
        return randomFirstName();
    }

    public static FirstName randomFirstName() {
        return FirstName.of(names().first().get());
    }
}
