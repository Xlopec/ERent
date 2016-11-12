package com.ua.erent.module.core.presentation.mvp.view.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Максим on 11/13/2016.
 */

public class SpaceDecorator extends RecyclerView.ItemDecoration {

    private int left;
    private int right;
    private int top;
    private int bottom;

    public SpaceDecorator() {
    }

    public SpaceDecorator(int space) {
        left = right = top = bottom = space;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = left < 0 ? 0 : left;
        outRect.right = right < 0 ? 0 : right;
        outRect.top = right < 0 ? 0 : top;
        outRect.bottom = bottom < 0 ? 0 : bottom;
    }
}
