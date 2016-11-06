package com.ua.erent.module.core.account.auth.user.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserForm;
import com.ua.erent.module.core.util.FormField;

import java.lang.reflect.Type;

/**
 * Created by Максим on 11/6/2016.
 */

public final class UserSerializer implements JsonSerializer<UserForm> {

    @Override
    public JsonElement serialize(UserForm src, Type typeOfSrc, JsonSerializationContext context) {

        final JsonObject json = new JsonObject();

        final FormField<String> emailField = src.getEmailField();

        if(!emailField.isEmpty()) {
            json.addProperty("email", emailField.getValue());
        }

        final FormField<String> usernameField = src.getUsernameField();

        if(!usernameField.isEmpty()) {
            json.addProperty("username", usernameField.getValue());
        }

        return json;
    }
}
