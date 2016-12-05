package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/3/2016.
 */

final class WithCategory implements IApiFilter {

    private final Collection<Long> categories;

    WithCategory(long category) {
        this.categories = new ArrayList<>(1);
        this.categories.add(category);
    }

    WithCategory(long... categories) {
        this.categories = new ArrayList<>(categories.length);

        for (final long categoryId : categories) {
            this.categories.add(categoryId);
        }
    }

    public void addCategory(long categoryId) {
        this.categories.add(categoryId);
    }

    public Collection<Long> getCategories() {
        return Collections.unmodifiableCollection(categories);
    }

    @Override
    public Map<String, String> toFilter() {

        final StringBuilder sb = new StringBuilder(categories.size() * 2 - 1);

        for (final long categoryId : categories) {
            sb.append(categoryId).append(',');
        }
        sb.setLength(sb.length() - 1);
        return Collections.singletonMap("category", sb.toString());
    }
}
