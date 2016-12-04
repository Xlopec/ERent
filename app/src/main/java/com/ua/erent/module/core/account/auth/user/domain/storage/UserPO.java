package com.ua.erent.module.core.account.auth.user.domain.storage;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 11/5/2016.
 */
@DatabaseTable(tableName = "User")
public final class UserPO {

    private static final String ID_COLUMN = "id";
    private static final String USERNAME_COLUMN = "username";
    private static final String EMAIL_COLUMN = "email";
    private static final String URL_COLUMN = "url";
    private static final String PHONE_COLUMN = "phone";
    private static final String SKYPE_COLUMN = "skype";

    @DatabaseField(id = true, index = true, columnName = ID_COLUMN, dataType = DataType.LONG)
    private long id;

    @DatabaseField(columnName = USERNAME_COLUMN, dataType = DataType.STRING, canBeNull = false)
    private String username;

    @DatabaseField(columnName = EMAIL_COLUMN, dataType = DataType.STRING, canBeNull = false)
    private String email;

    @DatabaseField(columnName = URL_COLUMN, dataType = DataType.STRING)
    private String url;

    @DatabaseField(columnName = PHONE_COLUMN, dataType = DataType.STRING)
    private String phone;

    @DatabaseField(columnName = SKYPE_COLUMN, dataType = DataType.STRING)
    private String skype;

    UserPO() {
    }

    public long getId() {
        return id;
    }

    public UserPO setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserPO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserPO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UserPO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getSkype() {
        return skype;
    }

    public UserPO setSkype(String skype) {
        this.skype = skype;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserPO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public String toString() {
        return "UserPO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", skype='" + skype + '\'' +
                '}';
    }
}
