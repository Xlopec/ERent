package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

final class WithLastId implements IApiFilter {

    enum Sign {
        GREATER(">%d"), LOWER("<%d");

        private final String str;

        Sign(String str) {
            this.str = str;
        }

        public String toFilterVal(long val) {
            return String.format(str, val);
        }
    }

    private final Sign sign;
    private final long lastId;

    WithLastId(Sign sign, long lastId) {
        this.sign = sign;
        this.lastId = lastId;
    }

    public Sign getSign() {
        return sign;
    }

    public long getLastId() {
        return lastId;
    }

    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("lastId", sign == null ? String.valueOf(lastId) : sign.toFilterVal(lastId));
    }
}
