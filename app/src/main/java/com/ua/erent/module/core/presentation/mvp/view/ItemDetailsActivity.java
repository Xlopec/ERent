package com.ua.erent.module.core.presentation.mvp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.erent.R;
import com.ua.erent.module.core.di.target.InjectableActivity;
import com.ua.erent.module.core.presentation.mvp.component.ItemDetailsComponent;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ItemDetailsView;
import com.ua.erent.module.core.presentation.mvp.view.util.IFutureBitmap;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public final class ItemDetailsActivity extends InjectableActivity<ItemDetailsActivity, ItemDetailsPresenter>
        implements ItemDetailsView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.item_gallery_image)
    protected ImageView galleryImageView;

    @BindView(R.id.fab)
    protected FloatingActionButton fab;

    @BindView(R.id.item_primary_info)
    protected TextView locationTextView;

    @BindView(R.id.item_description)
    protected TextView descriptionTextView;

    @BindView(R.id.item_pub_date)
    protected TextView pubDateTextView;

    @BindView(R.id.item_owner_avatar)
    protected ImageView avatarImageView;

    @BindView(R.id.item_owner_full_name)
    protected TextView usernameTextView;

    public ItemDetailsActivity() {
        super(R.layout.activity_item_details, ItemDetailsComponent.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.move));
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        galleryImageView.setOnClickListener(v -> presenter.onShowGallery(galleryImageView));
        fab.setOnClickListener(v -> presenter.onOpenDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if(id == R.id.action_email) {
            presenter.onComplain();
        } else if(id == android.R.id.home){
            finish();
        }

        return true;
    }

    @NotNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(@NotNull String title) {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void setGalleryPreviewImage(@NotNull IFutureBitmap bitmap) {
        final ViewGroup.LayoutParams params = galleryImageView.getLayoutParams();
        final WeakReference<ImageView> avatarRef = new WeakReference<>(galleryImageView);

        bitmap.fetch(params.width, params.height, this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bm -> {
                            if (avatarRef.get() != null)
                                galleryImageView.setImageBitmap(bm);
                        },
                        th -> {
                        });
    }

    @Override
    public void setMainInfo(@NotNull String mainInfo) {
        locationTextView.setText(mainInfo);
    }

    @Override
    public void setDescription(@NotNull String description) {
        descriptionTextView.setText(description);
    }

    @Override
    public void setPubDate(@NotNull String pubDate) {
        pubDateTextView.setText(pubDate);
    }

    @Override
    public void setUsername(@NotNull String username) {
        usernameTextView.setText(username);
    }

    @Override
    public void setAvatar(@NotNull IFutureBitmap bitmap) {
        final ViewGroup.LayoutParams params = avatarImageView.getLayoutParams();
        final WeakReference<ImageView> avatarRef = new WeakReference<>(avatarImageView);

        bitmap.fetch(params.width, params.height, this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bm -> {
                            if (avatarRef.get() != null)
                                avatarImageView.setImageBitmap(bm);
                        },
                        th -> {
                            if (avatarRef.get() != null)
                                avatarImageView.setImageResource(R.drawable.ic_account_circle_def_24dp);
                        });
    }

    @Override
    public void showText(@NotNull String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
