package org.example.entities;

import static java.util.Objects.isNull;

public enum Rating {

    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Rating getRatingByValue(String val) {
        if (isNull(val) || val.isEmpty()) {
            return null;
        }
        Rating[] values = Rating.values();
        for (Rating r : values) {
            if (r.value.equals(val)) {
                return r;
            }
        }
        return null;
    }
}
