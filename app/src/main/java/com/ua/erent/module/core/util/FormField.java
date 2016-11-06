package com.ua.erent.module.core.util;

/**
 * <p>
 *     Represents single form field
 * </p>
 * Created by Максим on 11/5/2016.
 */

public class FormField<T> {

    private final T t;
    private final boolean isEmpty;

    public FormField(T t) {
        this.t = t;
        this.isEmpty = false;
    }

    public FormField() {
        t = null;
        isEmpty = true;
    }

    public T getValue() {
        return t;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormField<?> formField = (FormField<?>) o;

        if (isEmpty != formField.isEmpty) return false;
        return t != null ? t.equals(formField.t) : formField.t == null;

    }

    @Override
    public int hashCode() {
        int result = t != null ? t.hashCode() : 0;
        result = 31 * result + (isEmpty ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FormField{" +
                "t=" + t +
                ", isEmpty=" + isEmpty +
                '}';
    }
}
