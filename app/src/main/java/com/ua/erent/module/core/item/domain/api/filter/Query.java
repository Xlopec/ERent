package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class Query implements IApiFilter {

    private final String query;

    public Query(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("q", query);
    }
}
