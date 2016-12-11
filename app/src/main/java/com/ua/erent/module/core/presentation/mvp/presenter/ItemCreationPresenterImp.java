package com.ua.erent.module.core.presentation.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.theartofdev.edmodo.cropper.CropImage;
import com.ua.erent.R;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.ItemCreationModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemCreationPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.view.ImageCropActivity;
import com.ua.erent.module.core.presentation.mvp.view.ItemCreateActivity;
import com.ua.erent.module.core.presentation.mvp.view.interfaces.ItemCreationView;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;

import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.getCameraIntents;
import static com.theartofdev.edmodo.cropper.CropImage.getGalleryIntents;

/**
 * Created by Максим on 12/9/2016.
 */

public final class ItemCreationPresenterImp extends ItemCreationPresenter {

    private static final String TAG = ItemCreationPresenterImp.class.getSimpleName();
    private static final String ARG_CATEGORIES_CACHE = "argCategoriesCache";
    private static final String ARG_BRANDS_CACHE = "argBrandsCache";
    private static final String ARG_REGIONS_CACHE = "argRegionsCache";
    private static final String ARG_FORM_CACHE = "argFormCache";

    private static final int REQ_CROP_IMAGE = 0x1;

    private ItemCreationForm form;

    private final ItemCreationModel model;
    private final Collection<Subscription> subscriptions;
    private final ArrayList<CategoryModel> categories;
    private final ArrayList<BrandModel> brands;
    private final ArrayList<RegionModel> regions;

    private static class ZipRes {
        private final Collection<CategoryModel> categories;
        private final Collection<BrandModel> brands;
        private final Collection<RegionModel> regions;

        private ZipRes(Collection<CategoryModel> categories, Collection<BrandModel> brands, Collection<RegionModel> regions) {
            this.categories = categories;
            this.brands = brands;
            this.regions = regions;
        }
    }

    public ItemCreationPresenterImp(ItemCreationModel model) {
        this.model = model;
        this.categories = new ArrayList<>(0);
        this.brands = new ArrayList<>(0);
        this.regions = new ArrayList<>(0);
        this.subscriptions = new ArrayList<>();
    }

    @Override
    protected void onViewAttached(@NotNull ItemCreateActivity view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {

            if (savedState == null) {
                form = new ItemCreationForm();
                setupCategories();
                setupBrands();
                setupRegions();

            } else {
                restoreState(savedState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelable(ARG_FORM_CACHE, form);
        outState.putParcelableArrayList(ARG_CATEGORIES_CACHE, categories);
        outState.putParcelableArrayList(ARG_BRANDS_CACHE, brands);
        outState.putParcelableArrayList(ARG_REGIONS_CACHE, regions);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroyed() {
        for (final Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }

    private void setupCategories() {

        if (isViewGone())
            throw new IllegalStateException();

        final Collection<CategoryModel> categoryModels = model.getCategories();

        if (categoryModels.isEmpty()) {
            // no cached categories available
            if (model.hasConnection()) {
                // pull them from the api server
                final Subscription subscription = model.fetchCategories()
                        .subscribe(categories -> {
                                    this.categories.clear();
                                    this.categories.addAll(categories);
                                },
                                th -> {
                                    if (!isViewGone()) {
                                        getView().showMessage(getView().getString(R.string.creation_categories_failure));
                                    }
                                    Log.w(TAG, "error on fetch categories#", th);
                                });
                subscriptions.add(subscription);
            } else {
                getView().showMessage(getView().getString(R.string.creation_categories_failure));
            }
        } else {
            categories.clear();
            categories.addAll(categoryModels);
        }
    }

    private void setupBrands() {

        if (isViewGone())
            throw new IllegalStateException();

        if (model.hasConnection()) {

            final Subscription subscription = model.fetchBrands()
                    .subscribe(brands -> {
                        this.brands.clear();
                        this.brands.addAll(brands);
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showMessage(getView().getString(R.string.creation_brands_failure));
                        }
                        Log.e(TAG, "error on fetch brands#", th);
                    });
            subscriptions.add(subscription);
        } else {

            final Subscription subscription = model.getNetworkObservable()
                    .filter(hasConnection -> hasConnection)
                    .flatMap(conn -> model.fetchBrands())
                    .subscribe(brands -> {
                        this.brands.clear();
                        this.brands.addAll(brands);
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showMessage(getView().getString(R.string.creation_brands_failure));
                        }
                        Log.e(TAG, "error on fetch brands#", th);
                    });

            subscriptions.add(subscription);
        }
    }

    private void setupRegions() {

        if (isViewGone())
            throw new IllegalStateException();

        if (model.hasConnection()) {

            final Subscription subscription = model.fetchRegions()
                    .subscribe(regions -> {
                        this.regions.clear();
                        this.regions.addAll(regions);
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showMessage(getView().getString(R.string.creation_regions_failure));
                        }
                        Log.e(TAG, "error on fetch regions#", th);
                    });
            subscriptions.add(subscription);
        } else {

            final Subscription subscription = model.getNetworkObservable()
                    .filter(hasConnection -> hasConnection)
                    .flatMap(conn -> model.fetchRegions())
                    .subscribe(regions -> {
                        this.regions.clear();
                        this.regions.addAll(regions);
                    }, th -> {
                        if (!isViewGone()) {
                            getView().showMessage(getView().getString(R.string.creation_regions_failure));
                        }
                        Log.e(TAG, "error on fetch regions#", th);
                    });

            subscriptions.add(subscription);
        }
    }

    @Override
    public Collection<ItemCreationView.SelectableModel> onGetCategories() {

        final ArrayList<ItemCreationView.SelectableModel> result = new ArrayList<>(categories.size());

        for (final CategoryModel category : categories) {
            result.add(new ItemCreationView.SelectableModel(category.getId(), category.getTitle()));
        }
        return result;
    }

    @Override
    public Collection<ItemCreationView.SelectableModel> onGetBrands() {

        final ArrayList<ItemCreationView.SelectableModel> result = new ArrayList<>(brands.size());

        for (final BrandModel brandModel : brands) {
            result.add(new ItemCreationView.SelectableModel(brandModel.getId(), brandModel.getName()));
        }
        return result;
    }

    @Override
    public Collection<ItemCreationView.SelectableModel> onGetRegions() {

        final ArrayList<ItemCreationView.SelectableModel> result = new ArrayList<>(regions.size());

        for (final RegionModel regionModel : regions) {
            result.add(new ItemCreationView.SelectableModel(regionModel.getId(), regionModel.getName()));
        }
        return result;
    }

    @Override
    public void onSetCategory(long id) {
        form.setCategoryId(id);
    }

    @Override
    public void onSetRegion(long id) {
        form.setRegionId(id);
    }

    @Override
    public void onSetBrand(long id) {
        form.setBrandId(id);
    }

    @Override
    public void onCreateItem(@NotNull String name, @NotNull String description, @NotNull String price) {

        if (form == null)
            throw new IllegalStateException("form wasn't created");

        if (form.getCategoryId() <= 0) {
            getView().showMessage(getView().getString(R.string.creation_category_not_set));
            return;
        }

        if (form.getRegionId() <= 0) {
            getView().showMessage(getView().getString(R.string.creation_region_not_set));
            return;
        }

        if (form.getBrandId() <= 0) {
            getView().showMessage(getView().getString(R.string.creation_brand_not_set));
            return;
        }

        try {
            form.setPrice(Double.valueOf(price));
        } catch (final Exception e) {
            getView().showMessage(e.getMessage());
            return;
        }

        form.setName(name);
        form.setDescription(description);

        model.createItem(form)
                .doOnSubscribe(() -> getView().showProgress(getView().getString(R.string.creation_progress)))
                .doOnTerminate(getView()::hideProgress)
                .subscribe(aVoid -> {
                            getView().showMessage(getView().getString(R.string.creation_progress_success, form.getName()));
                        },
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(th.getMessage());
                            }
                        });
    }

    @Override
    public void onRefresh() {

        Observable.zip(model.fetchCategories(), model.fetchBrands(), model.fetchRegions(), ZipRes::new)
                .doOnSubscribe(() -> getView().showProgress(getView().getString(R.string.creation_refreshing)))
                .doOnTerminate(getView()::hideProgress)
                .subscribe(res -> {
                            categories.clear();
                            categories.addAll(res.categories);
                            brands.clear();
                            brands.addAll(res.brands);
                            regions.clear();
                            regions.addAll(res.regions);
                        },
                        th -> {
                            if (!isViewGone()) {
                                getView().showMessage(getView().getString(R.string.creation_categories_failure));
                            }
                            Log.w(TAG, "error on refresh#", th);
                        });
    }

    @Override
    public void onDeleteImage(@NotNull Uri uri) {
        form.removeUri(uri);
    }

    @Override
    public void onDeleteImages() {
        form.removeUris();
        getView().clearImages();
    }

    @Override
    public void onSelectImage() {
        getView().startActivityForResult(getPickImageChooserIntent(
                getView(), getView().getString(R.string.creation_pick_dialog_title), false), PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {

                final Uri imageUri = CropImage.getPickImageResultUri(getView(), data);
                final Intent intent = new Intent(getView(), ImageCropActivity.class);

                intent.putExtra(ImageCropActivity.ARG_IMG_URI, imageUri);
                intent.putExtra(ImageCropActivity.ARG_FIXED_ASPECT_RATIO, true);
                intent.putExtra(ImageCropActivity.ARG_ASPECT_RATIO_X, 1);
                intent.putExtra(ImageCropActivity.ARG_ASPECT_RATIO_Y, 1);
                intent.putExtra(ImageCropActivity.ARG_SHAPE, ImageCropActivity.Shape.RECT);
                getView().startActivityForResult(intent, REQ_CROP_IMAGE);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                handleAvatarUri(result.getUri());

            } else if (requestCode == REQ_CROP_IMAGE) {
                handleAvatarUri(data.getParcelableExtra(ImageCropActivity.ARG_CROPPED_IMG_URI));
            } else {
                Log.w(TAG, "Unknown request code " + requestCode);
            }

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            final Exception error = result.getError();
            getView().showMessage(error.toString());
        } else {
            Log.w(TAG, "Unknown result code " + resultCode);
        }
    }

    private void handleAvatarUri(@NotNull Uri uri) {
        form.addUri(uri);
        getView().addImage(ImageUtils.uriBitmap(uri));
    }

    private void restoreState(@NotNull Bundle state) {

        form = Preconditions.checkNotNull(state.getParcelable(ARG_FORM_CACHE));
        categories.addAll(state.getParcelableArrayList(ARG_CATEGORIES_CACHE));
        brands.addAll(state.getParcelableArrayList(ARG_BRANDS_CACHE));
        regions.addAll(state.getParcelableArrayList(ARG_REGIONS_CACHE));

        for(final Uri uri : form.getUris()) {
            getView().addImage(ImageUtils.uriBitmap(uri));
        }
    }

    private static Intent getPickImageChooserIntent(@NonNull Context context, CharSequence title, boolean includeDocuments) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        allIntents.addAll(getCameraIntents(context, packageManager));

        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        allIntents.addAll(galleryIntents);

        final Intent target = allIntents.get(allIntents.size() - 1);
        allIntents.remove(allIntents.size() - 1);

        // Create a chooser from the main  intent
        final Intent chooserIntent = Intent.createChooser(target, title);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

}
