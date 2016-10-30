package com.ua.erent.module.core.account.auth.domain.api.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 10/30/2016.
 */
@DatabaseTable(tableName = "Session")
public final class SessionPO {

    public static final String ID_COLUMN = "id";
    public static final String LOGIN_COLUMN = "login";
    public static final String TOKEN_COLUMN = "token";
    public static final String TOKEN_TYPE = "tokenType";

    @DatabaseField(id = true, index = true, columnName = ID_COLUMN, dataType = DataType.INTEGER, canBeNull = false)
    private int id;

    @DatabaseField(columnName = LOGIN_COLUMN, dataType = DataType.STRING, canBeNull = false)
    private String login;

    @DatabaseField(columnName = TOKEN_COLUMN, dataType = DataType.STRING, canBeNull = false)
    private String token;

    @DatabaseField(columnName = TOKEN_TYPE, dataType = DataType.STRING, canBeNull = false)
    private String tokenType;

    public SessionPO() {
    }

    public SessionPO(int id, String login, String token, String tokenType) {
        this.id = id;
        this.login = login;
        this.token = token;
        this.tokenType = tokenType;
    }

    public int getId() {
        return id;
    }

    public SessionPO setId(int id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public SessionPO setToken(String token) {
        this.token = token;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public SessionPO setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public SessionPO setLogin(String login) {
        this.login = login;
        return this;
    }

    @Override
    public String toString() {
        return "SessionPO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
