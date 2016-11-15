package com.ua.erent.module.core.item.domain.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Максим on 11/15/2016.
 */

final class CategoriesResponse {

    @SerializedName("id")
    public long id;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;

}
