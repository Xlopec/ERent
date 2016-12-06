package com.ua.erent.module.core.presentation.mvp.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;

import dagger.internal.Preconditions;
import rx.android.schedulers.AndroidSchedulers;

public final class GalleryActivity extends AppCompatActivity {

    public static final String ARG_IMAGES = "argImages";
    private static final String ARG_IMAGE_URLS_CACHE = "argImageUrlsCache";
    private static final String ARG_MENU_IMAGE_URL = "argMenuImageUrl";

    private class Adapter extends PagerAdapter {

        private final ArrayList<IUrlFutureBitmap> images;

        Adapter() {
            this.images = new ArrayList<>(0);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.gallery_image_item, container, false);

            final IParcelableFutureBitmap futureBitmap = images.get(position);
            final ImageView photoView = (ImageView) view.findViewById(R.id.image);
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            final WeakReference<ImageView> reference = new WeakReference<>(photoView);

            if (position == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoView.setTransitionName(getString(R.string.gallery_image_trans_name));
            }

            container.addView(view);
            futureBitmap.fetch(params.width, params.height, GalleryActivity.this)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            bm -> {
                                if (reference.get() != null)
                                    photoView.setImageBitmap(bm);
                            },
                            th -> {
                                if (reference.get() != null)
                                    photoView.setImageResource(R.drawable.image_placeholder_photo);
                            });

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        IUrlFutureBitmap getImage(int index) {
            return images.get(index);
        }

        void addAll(Collection<? extends IUrlFutureBitmap> bitmaps) {
            images.addAll(Preconditions.checkNotNull(bitmaps));
        }

        void onSaveInstanceState(Bundle out) {
            out.putParcelableArrayList(ARG_IMAGE_URLS_CACHE, images);
        }

        void onRestoreInstanceState(Bundle savedState) {
            final ArrayList<IUrlFutureBitmap> cache =
                    savedState.getParcelableArrayList(ARG_IMAGE_URLS_CACHE);

            if (cache != null) {
                images.addAll(cache);
            }
        }

    }

    private final Adapter adapter;

    private IUrlFutureBitmap currentImage;

    public GalleryActivity() {
        adapter = new Adapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        final Bundle params = getIntent().getExtras();

        if (savedInstanceState == null && params == null)
            throw new IllegalArgumentException("either arguments wasn't passed or state wasn't saved");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final Transition transition = TransitionInflater.from(this)
                    .inflateTransition(android.R.transition.move);

            window.setSharedElementEnterTransition(transition);
            window.setSharedElementExitTransition(transition);
            window.setSharedElementsUseOverlay(false);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (savedInstanceState == null) {

            final ArrayList<IUrlFutureBitmap> argArr =
                    getIntent().getParcelableArrayListExtra(GalleryActivity.ARG_IMAGES);
            adapter.addAll(argArr);
        } else {
            adapter.onRestoreInstanceState(savedInstanceState);
        }

        final ViewPager imagePager = (ViewPager) findViewById(R.id.image_view_pager);

        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                final ActionBar actionBar = getSupportActionBar();

                if (actionBar != null) {
                    currentImage = adapter.getImage(position);
                    invalidateOptionsMenu();
                    actionBar.setTitle(getString(R.string.gallery_pager_title, position + 1, adapter.getCount()));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        imagePager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        currentImage = adapter.getImage(0);
        invalidateOptionsMenu();

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.gallery_pager_title, imagePager.getCurrentItem() + 1, adapter.getCount()));
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final Uri uri = Uri.parse(currentImage.getUrl().toExternalForm());
        final Intent copyIntent = new Intent();
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);

        copyIntent.putExtra(ARG_MENU_IMAGE_URL, uri);
        menu.findItem(R.id.action_copy_url).setIntent(copyIntent);
        menu.findItem(R.id.action_open_browser).setIntent(browserIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int id = item.getItemId();

        if (id == R.id.action_copy_url) {
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            final ClipData clip = ClipData.newUri(getContentResolver(), getString(R.string.gallery_clip_title),
                    item.getIntent().getParcelableExtra(ARG_MENU_IMAGE_URL));
            clipboard.setPrimaryClip(clip);

        } else if (id == R.id.action_open_browser) {
            startActivity(item.getIntent());

        } else if (id == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this);
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        adapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
