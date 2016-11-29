package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

public final class WithLastId implements IApiFilter {

    private final long lastId;

    public WithLastId(long lastId) {
        this.lastId = lastId;
    }

    public long getLastId() {
        return lastId;
    }


    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("lastId", String.valueOf(lastId));
    }
}
