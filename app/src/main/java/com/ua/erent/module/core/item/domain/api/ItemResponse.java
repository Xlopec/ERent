package com.ua.erent.module.core.item.domain.api;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * <p>
 *     Represents item server response json
 * </p>
 * Created by Максим on 11/8/2016.
 */

final class ItemResponse {

    @SerializedName("id")
    public long id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("price")
    public int price;
    @SerializedName("publication_date")
    public DateTime publicationDate;
    @SerializedName("owner")
    public Owner owner;
    @SerializedName("brand")
    public Brand brand;
    @SerializedName("region")
    public Region region;
    @SerializedName("categories")
    public Category [] categories;
    @SerializedName("photos")
    public String [] photos;

    final static class Category {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String description;
    }

    final static class Owner {
        @SerializedName("id")
        public long id;
        @SerializedName("username")
        public String username;
        @SerializedName("avatar")
        public String avatarUrl;
    }

    final static class Brand {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("description")
        public String description;
    }

    final static class Region {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
    }

}
