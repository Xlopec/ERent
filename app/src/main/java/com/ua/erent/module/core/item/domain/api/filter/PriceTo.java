package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class PriceTo implements IApiFilter {

    private final BigDecimal to;

    public PriceTo(BigDecimal to) {
        this.to = to;
    }

    public BigDecimal getTo() {
        return to;
    }

    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("priceTo", to.toPlainString());
    }
}
