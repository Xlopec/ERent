package com.ua.erent.module.core.item.domain.api.filter;

import com.ua.erent.module.core.networking.util.IApiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Максим on 12/4/2016.
 */

final class Owner implements IApiFilter {

    private final Collection<Long> owners;

    Owner(long ownerId) {
        this.owners = new ArrayList<>(1);
        this.owners.add(ownerId);
    }

    Owner(long... owners) {
        this.owners = new ArrayList<>(owners.length);

        for (final long ownerId : owners) {
            this.owners.add(ownerId);
        }
    }

    public void addOwner(long ownerId) {
        this.owners.add(ownerId);
    }

    public Collection<Long> getOwners() {
        return Collections.unmodifiableCollection(owners);
    }

    @Override
    public Map<String, String> toFilter() {

        final StringBuilder sb = new StringBuilder(owners.size() * 2 - 1);

        for (final long ownerId : owners) {
            sb.append(ownerId).append(',');
        }
        sb.setLength(sb.length() - 1);
        return Collections.singletonMap("owner", sb.toString());
    }
}
