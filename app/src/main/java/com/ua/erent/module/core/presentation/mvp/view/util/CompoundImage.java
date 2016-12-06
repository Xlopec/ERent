package com.ua.erent.module.core.presentation.mvp.view.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;

import com.ua.erent.R;

/**
 * Created by Максим on 12/5/2016.
 */

public class CompoundImage extends CompoundButton {

    private static final String TAG = CompoundImage.class.getSimpleName();

    public CompoundImage(Context context) {
        super(context);
    }

    public CompoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CompoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CompoundImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CompoundImage, defStyleAttr, defStyleRes);
        int animResource = a.getResourceId(R.styleable.CompoundImage_drawableAnim, 0);
        String customFont = a.getString(R.styleable.CompoundImage_font);
        setBackgroundResource(animResource);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf;

        try {
            tf = Typefaces.get(ctx, asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
