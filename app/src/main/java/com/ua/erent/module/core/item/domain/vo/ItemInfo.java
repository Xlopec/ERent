package com.ua.erent.module.core.item.domain.vo;

import java.math.BigDecimal;

/**
 * Created by Максим on 11/7/2016.
 */

public final class ItemInfo {

    private final String name;
    private final String description;
    private final BigDecimal price;

    public ItemInfo(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
