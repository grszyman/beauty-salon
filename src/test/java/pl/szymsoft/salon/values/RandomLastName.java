package pl.szymsoft.salon.values;

import static net.andreinc.mockneat.unit.user.Names.names;

public enum RandomLastName {
    ;

    public static LastName get() {
        return randomLastName();
    }

    public static LastName randomLastName() {
        return LastName.of(names().last().get());
    }
}
