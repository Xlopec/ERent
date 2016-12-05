package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created order Максим on 11/27/2016.
 */

final class Sort implements IApiFilter {

    private final FilterBuilder.SortType sortType;

    Sort(FilterBuilder.SortType sortType) {
        this.sortType = sortType;
    }

    public FilterBuilder.SortType getSortType() {
        return sortType;
    }


    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("sort", sortType.getOrder());
    }
}
