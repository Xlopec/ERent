package com.ua.erent.module.core.presentation.mvp.view.util;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Максим on 11/12/2016.
 */

public final class ColorUtils {

    private static final int[] colors = {
            Color.RED,
            Color.BLUE,
            Color.CYAN,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA
    };

    private ColorUtils() {
        throw new IllegalStateException("shouldn't be instantiated");
    }

    public static int tryParseString(String color, int defaultColor) {

        if(color == null) return defaultColor;

        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return defaultColor;
        }
    }

    public static int randomColor() {
        return colors[new Random().nextInt(colors.length)];
    }

}
