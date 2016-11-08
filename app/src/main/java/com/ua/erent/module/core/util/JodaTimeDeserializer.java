package com.ua.erent.module.core.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

/**
 * <p>
 *     Deserialize json into {@linkplain DateTime} instance
 * </p>
 * Created by Максим on 11/8/2016.
 */

public final class JodaTimeDeserializer implements JsonDeserializer<DateTime> {

    private static final String TAG = JodaTimeDeserializer.class.getSimpleName();

    private final DateTimeFormatter formatter;

    public JodaTimeDeserializer(@NotNull String format) {
        this.formatter = DateTimeFormat.forPattern(format).withZone(DateTimeZone.forOffsetHours(0));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final String date = json.getAsString();

        try {
            return formatter.parseDateTime(date).withZone(DateTimeZone.getDefault());
        } catch (final Exception e) {
            Log.e(TAG, "failed to parse due to following exception", e);
            return null;
        }
    }
}
