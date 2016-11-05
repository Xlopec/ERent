package com.ua.erent.module.core.account.auth.user.domain.storage;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 11/5/2016.
 */
@DatabaseTable(tableName = "User")
public final class UserPO {

    public static final String ID_COLUMN = "id";
    public static final String USERNAME_COLUMN = "username";
    public static final String EMAIL_COLUMN = "email";

    @DatabaseField(id = true, index = true, columnName = ID_COLUMN, dataType = DataType.LONG)
    private long id;

    @DatabaseField(columnName = USERNAME_COLUMN, dataType = DataType.STRING, canBeNull = false, throwIfNull = true)
    private String username;

    @DatabaseField(columnName = EMAIL_COLUMN, dataType = DataType.STRING, canBeNull = false, throwIfNull = true)
    private String email;

    public UserPO() {
    }

    public String getEmail() {
        return email;
    }

    public UserPO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserPO setUsername(String username) {
        this.username = username;
        return this;
    }

    public long getId() {
        return id;
    }

    public UserPO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "UserPO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
