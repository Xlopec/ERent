package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

final class Order implements IApiFilter {

    private final FilterBuilder.OrderBy orderBy;

    Order(FilterBuilder.OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public FilterBuilder.OrderBy getOrderBy() {
        return orderBy;
    }


    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("orderBy", orderBy.getField());
    }
}
