package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class Brand implements IApiFilter {

    private final Collection<Long> brands;

    Brand(long brandId) {
        this.brands = new ArrayList<>(1);
        this.brands.add(brandId);
    }

    Brand(long... brands) {
        this.brands = new ArrayList<>(brands.length);

        for (final long brandId : brands) {
            this.brands.add(brandId);
        }
    }

    public void addBrand(long brandId) {
        this.brands.add(brandId);
    }

    public Collection<Long> getBrands() {
        return Collections.unmodifiableCollection(brands);
    }

    @Override
    public Map<String, String> toFilter() {

        final StringBuilder sb = new StringBuilder(brands.size() * 2 - 1);

        for (final long ownerId : brands) {
            sb.append(ownerId).append(',');
        }
        sb.setLength(sb.length() - 1);
        return Collections.singletonMap("brand", sb.toString());
    }
}
