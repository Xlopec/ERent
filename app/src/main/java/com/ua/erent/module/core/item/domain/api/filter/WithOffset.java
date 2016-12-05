package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

final class WithOffset implements IApiFilter {

    private final long offset;

    WithOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }


    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("offset", String.valueOf(offset));
    }
}
