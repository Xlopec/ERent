package com.ua.erent.module.core.presentation.mvp.view;

/**
 * Created by Максим on 12/6/2016.
 */
public enum ContentType {

    LOADER(0), CONTENT(1);

    private final int valId;

    ContentType(int valId) {
        this.valId = valId;
    }

    public int getValId() {
        return valId;
    }

    public static ContentType forId(int id) {
        for (final ContentType t : ContentType.values()) {
            if (t.valId == id) return t;
        }
        return null;
    }
}
