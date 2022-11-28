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

    public Rating getRatingByValue(String val) {
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

//    @Converter(autoApply = true)
//    public static class RatingConverter implements AttributeConverter<Rating, String> {
//
//        @Override
//        public String convertToDatabaseColumn(Rating rating) {
//            if (rating == null) {
//                return null;
//            }
//            return rating.getValue();
//        }
//
//        @Override
//        public Rating convertToEntityAttribute(String val) {
//            if (val == null) {
//                return null;
//            }
//
//            return Stream.of(Rating.values())
//                    .filter(c -> c.getValue().equals(val))
//                    .findFirst()
//                    .orElseThrow(IllegalArgumentException::new);
//        }
//    }


}
