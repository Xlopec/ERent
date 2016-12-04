package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;
import com.ua.erent.module.core.util.IBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 11/27/2016.
 */

public final class FilterBuilder implements IBuilder<IApiFilter> {

    private final Map<Class<? extends IApiFilter>, IApiFilter> classToFilter;

    public enum OrderBy {

        NAME("name"), ID("id"), PRICE("price"), PUB_DATE("publicationDate");

        private final String by;

        OrderBy(String by) {
            this.by = by;
        }

        public String getField() {
            return by;
        }
    }

    public enum SortType {

        ASC("asc"), DESC("desc");

        private final String order;

        SortType(String order) {
            this.order = order;
        }

        public String getOrder() {
            return order;
        }
    }

    public FilterBuilder() {
        this.classToFilter = new HashMap<>(5);
    }

    public FilterBuilder withLastIdGreater(long id) {
        classToFilter.put(WithLastId.class, new WithLastId(WithLastId.Sign.GREATER, id));
        return this;
    }

    public FilterBuilder withLastIdLower(long id) {
        classToFilter.put(WithLastId.class, new WithLastId(WithLastId.Sign.LOWER, id));
        return this;
    }

    public FilterBuilder withLimit(long limit) {
        classToFilter.put(WithLimit.class, new WithLimit(limit));
        return this;
    }

    public FilterBuilder withOffset(long offset) {
        classToFilter.put(WithOffset.class, new WithOffset(offset));
        return this;
    }

    public FilterBuilder orderBy(@NotNull OrderBy orderBy) {
        classToFilter.put(Order.class, new Order(orderBy));
        return this;
    }

    public FilterBuilder withCategory(long category) {
        classToFilter.put(WithCategory.class, new WithCategory(category));
        return this;
    }

    public FilterBuilder withCategory(long...categories) {
        classToFilter.put(WithCategory.class, new WithCategory(categories));
        return this;
    }

    public FilterBuilder sort(@NotNull SortType sortType) {
        classToFilter.put(Sort.class, new Sort(sortType));
        return this;
    }

    @Override
    public IApiFilter build() {
        return () -> {

            final Map<String, String> keyVal = new HashMap<>(classToFilter.size());

            for (final IApiFilter filter : classToFilter.values()) {
                keyVal.putAll(filter.toFilter());
            }

            return keyVal;
        };
    }
}
