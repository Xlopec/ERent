package com.ua.erent.module.core.item.domain.api;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by Максим on 12/11/2016.
 */

final class ItemResponse {

    @SerializedName("count")
    public int count;
    @SerializedName("data")
    public Collection<RespItem> items;

}
