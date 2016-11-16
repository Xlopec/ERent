package com.ua.erent.module.core.item.domain.storage;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 11/15/2016.
 */
@DatabaseTable(tableName = "Category")
public final class CategoryPO {

    @DatabaseField(id = true, index = true, columnName = "id", dataType = DataType.LONG)
    private long id;
    @DatabaseField(columnName = "title", dataType = DataType.STRING, canBeNull = false)
    private String title;
    @DatabaseField(columnName = "description", dataType = DataType.STRING, canBeNull = false)
    private String description;

    public CategoryPO() {
    }

    public CategoryPO(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
