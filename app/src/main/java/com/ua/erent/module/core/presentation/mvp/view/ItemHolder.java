package com.ua.erent.module.core.presentation.mvp.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;

/**
 * Created by Максим on 12/6/2016.
 */
class ItemHolder extends TypedViewHolder {

    private long id;
    private final ImageView avatar;
    private final ImageView photo;
    private final TextView title;
    private final TextView subTitle;
    private final TextView description;
    private final ImageButton actionMenu;

    private IFutureBitmap avatarBm;
    private IFutureBitmap photoBm;

    ItemHolder(View itemView, ContentType type) {
        super(itemView, type);

        if (type == ContentType.CONTENT) {
            avatar = (ImageView) itemView.findViewById(R.id.item_user_avatar);
            title = (TextView) itemView.findViewById(R.id.item_title);
            subTitle = (TextView) itemView.findViewById(R.id.item_sub_title);
            description = (TextView) itemView.findViewById(R.id.item_description);
            photo = (ImageView) itemView.findViewById(R.id.item_photo);
            actionMenu = (ImageButton) itemView.findViewById(R.id.item_action_menu);
        } else {
            avatar = photo = null;
            title = subTitle = description = null;
            actionMenu = null;
        }
    }

    public void setAvatarBm(IFutureBitmap avatarBm) {
        this.avatarBm = avatarBm;
    }

    public void setPhotoBm(IFutureBitmap photoBm) {
        this.photoBm = photoBm;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getSubTitle() {
        return subTitle;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageButton getActionMenu() {
        return actionMenu;
    }

    public IFutureBitmap getAvatarBm() {
        return avatarBm;
    }

    public IFutureBitmap getPhotoBm() {
        return photoBm;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
