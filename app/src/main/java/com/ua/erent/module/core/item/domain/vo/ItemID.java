package com.ua.erent.module.core.item.domain.vo;

import java.util.Locale;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemID {

    private final long id;

    public ItemID(long id) {

        if (id < 1)
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(), "id < 1, was %d", id));

        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemID itemID = (ItemID) o;

        return id == itemID.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ItemID{" +
                "id=" + id +
                '}';
    }
}
