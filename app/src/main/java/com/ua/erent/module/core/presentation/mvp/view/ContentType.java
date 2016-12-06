package com.ua.erent.module.core.presentation.mvp.view;

/**
 * Created by Максим on 12/6/2016.
 */
public enum ContentType {

    LOADER(0), CONTENT(1);

    private final int type;

    ContentType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ContentType forId(int id) {
        for (final ContentType t : ContentType.values()) {
            if (t.type == id) return t;
        }
        return null;
    }
}
