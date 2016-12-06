package com.ua.erent.module.core.presentation.mvp.view;

/**
 * Created by Максим on 12/6/2016.
 */
public class RecyclerItem {

    private static long gen;

    private final long id;
    private final ContentType type;
    private final Object payload;

    RecyclerItem(ContentType type, Object payload) {
        this.id = ++gen;
        this.type = type;
        this.payload = payload;
    }

    public long getId() {
        return id;
    }

    public ContentType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    <T> T getPayload() {
        return (T) payload;
    }
}
