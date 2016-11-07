package com.ua.erent.module.core.item.domain.vo;

/**
 * Created by Максим on 11/7/2016.
 */

public final class Brand {

    private final String name;
    private final String description;

    public Brand(String name, String description) {
        // todo validation
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
