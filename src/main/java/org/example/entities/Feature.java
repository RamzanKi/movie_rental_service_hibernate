package org.example.entities;

import static java.util.Objects.isNull;

public enum Feature {

    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");

    private final String value;

    Feature(String value) {
        this.value = value;
    }

    public Feature getFeatureByValue(String val) {
        if (isNull(val) || val.isEmpty()) {
            return null;
        }
        Feature[] values = Feature.values();
        for (Feature feature : values) {
            if (feature.value.equals(val)) {
                return feature;
            }
        }
        return null;
    }
}

