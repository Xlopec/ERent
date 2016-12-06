package com.ua.erent.module.core.presentation.mvp.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 12/6/2016.
 */
public abstract class TypedViewHolder extends RecyclerView.ViewHolder {

    private final ContentType type;

    TypedViewHolder(@NotNull View itemView, @NotNull ContentType type) {
        super(itemView);
        this.type = Preconditions.checkNotNull(type);
    }

    public ContentType getType() {
        return type;
    }
}
