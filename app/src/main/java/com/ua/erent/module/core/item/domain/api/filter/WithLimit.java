package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

final class WithLimit implements IApiFilter {

    private final long limit;

    WithLimit(long limit) {
        this.limit = limit;
    }

    public long getLimit() {
        return limit;
    }


    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("limit", String.valueOf(limit));
    }
}
