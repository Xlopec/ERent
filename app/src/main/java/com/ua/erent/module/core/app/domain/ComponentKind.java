package com.ua.erent.module.core.app.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Represents component kind for {@linkplain com.ua.erent.module.core.app.domain.interfaces.IAppLifecycleManager}.
 * Each kind differs in its priority, e.g. component tagged with {@linkplain ComponentKind#APP_SERVICE} will be restored
 * earlier than {@linkplain ComponentKind#VIEW_MODEL}
 * </p>
 * Created by Максим on 10/28/2016.
 */

public enum ComponentKind {
    /**
     * Represents application service
     * which runs all time app exists
     */
    APP_SERVICE(2),
    /**
     * Represents single view, e.g. Model in MVP architecture
     */
    VIEW_MODEL(1);

    static {

        final List<ComponentKind> sortedList = new ArrayList<>(ComponentKind.values().length);
        sortedList.add(ComponentKind.VIEW_MODEL);
        sortedList.add(ComponentKind.APP_SERVICE);

        Collections.sort(sortedList, (o1, o2) -> (o1.getPriority() < o2.getPriority()) ?
                -1 : ((o1.getPriority() == o2.getPriority()) ? 0 : 1));

        ComponentKind.prioritySet = Collections.unmodifiableSet(new HashSet<>(sortedList));
    }

    private static Set<ComponentKind> prioritySet;
    private final int priority;

    ComponentKind(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static Set<ComponentKind> prioritySet() {
        return prioritySet;
    }

}
