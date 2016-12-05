package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class PriceFrom implements IApiFilter {

    private final BigDecimal from;

    public PriceFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getFrom() {
        return from;
    }

    @Override
    public Map<String, String> toFilter() {
        return Collections.singletonMap("priceFrom", from.toPlainString());
    }
}
