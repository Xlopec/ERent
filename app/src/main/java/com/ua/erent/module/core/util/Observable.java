package com.ua.erent.module.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 *     Utility class for observable-observer pattern realization
 * </p>
 * Created by Максим on 10/14/2016.
 */
public class Observable <T> {

    private final Collection<IObserver<T>> observers;

    private T current;
    private T previous;

    public Observable() {
        this(null, 10);
    }

    public Observable(T initial) {
        this(initial, 10);
    }

    public Observable(T initial, int capacity) {
        this.current = initial;
        this.observers = new ArrayList<>(capacity);
    }

    public T getCurrent() {
        return current;
    }

    public T getPrevious() {
        return previous;
    }

    public boolean addObserver(@NotNull IObserver<T> observer) {
        return observers.add(observer);
    }

    public boolean removeObserver(@NotNull IObserver<T> observer) {
        return observers.remove(observer);
    }

    public void removeAll() {
        observers.clear();
    }

    public void setValue(T t) {

        synchronized (this) {
            // notify observers
            for(final IObserver<T> IObserver : observers) {
                IObserver.onValueChanged(this, previous, current);
            }
            // swap values
            previous = current;
            current = t;
        }
    }

}
