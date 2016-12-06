package com.ua.erent.module.core.presentation.mvp.presenter.model;

/**
 * Created by Максим on 12/6/2016.
 */

public final class RegionModel {

    private final long id;
    private final String name;

    public RegionModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegionModel that = (RegionModel) o;

        if (id != that.id) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RegionModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
