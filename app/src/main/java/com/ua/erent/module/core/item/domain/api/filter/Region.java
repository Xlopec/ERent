package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class Region implements IApiFilter {

    private final Collection<Long> regions;

    Region(long regionId) {
        this.regions = new ArrayList<>(1);
        this.regions.add(regionId);
    }

    Region(long... regions) {
        this.regions = new ArrayList<>(regions.length);

        for (final long regionId : regions) {
            this.regions.add(regionId);
        }
    }

    public void addRegion(long regionId) {
        this.regions.add(regionId);
    }

    public Collection<Long> getRegions() {
        return Collections.unmodifiableCollection(regions);
    }

    @Override
    public Map<String, String> toFilter() {

        final StringBuilder sb = new StringBuilder(regions.size() * 2 - 1);

        for (final long ownerId : regions) {
            sb.append(ownerId).append(',');
        }
        sb.setLength(sb.length() - 1);
        return Collections.singletonMap("region", sb.toString());
    }
}
