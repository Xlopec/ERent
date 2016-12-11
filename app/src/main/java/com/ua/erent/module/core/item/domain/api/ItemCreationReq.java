package com.ua.erent.module.core.item.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 12/9/2016.
 */

final class ItemCreationReq {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("price")
    double price;
    @SerializedName("brand")
    long brandId;
    @SerializedName("region")
    long regionId;
    @SerializedName("categories")
    long [] categoryIds;
    @SerializedName("isVisible")
    boolean visibility;

    @Override
    public String toString() {
        return "ItemCreationReq{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", brandId=" + brandId +
                ", regionId=" + regionId +
                ", categoryIds=" + categoryIds +
                ", visibility=" + visibility +
                '}';
    }
}
