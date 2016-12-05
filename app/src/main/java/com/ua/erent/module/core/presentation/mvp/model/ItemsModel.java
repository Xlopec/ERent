package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;
import android.content.Intent;

import com.ua.erent.R;
import com.ua.erent.module.core.account.auth.domain.IAuthAppService;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.api.filter.FilterBuilder;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.interfaces.ItemDetailsPresenter;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.ItemDetailsActivity;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;
import com.ua.erent.module.core.util.MyTextUtil;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Максим on 11/28/2016.
 */

public final class ItemsModel implements IItemsModel {

    private final IItemAppService itemAppService;
    private final IAuthAppService authAppService;
    private final Context context;

    @Inject
    public ItemsModel(Context context, IItemAppService itemAppService, IAuthAppService authAppService) {
        this.itemAppService = itemAppService;
        this.context = context;
        this.authAppService = authAppService;
    }

    @Override
    public boolean isSessionAlive() {
        return authAppService.isSessionAlive();
    }

    @Override
    public Observable<Collection<ItemModel>> fetch(long categoryId, long limit) {
        return itemAppService.fetchItems(new FilterBuilder()
                .withCategory(categoryId)
                .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                .withLimit(limit)
                .build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(long categoryId, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdGreater(lastId)
                        .withCategory(categoryId)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(long categoryId, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withCategory(categoryId)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .withLastIdLower(lastId)
                        .withLimit(limit)
                        .build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(@NotNull String query, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdGreater(lastId)
                        .withQuery(query)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(@NotNull String query, long limit, long lastId) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withLastIdLower(lastId)
                        .withQuery(query)
                        .orderBy(FilterBuilder.OrderBy.PUB_DATE)
                        .sort(FilterBuilder.SortType.ASC)
                        .withLimit(limit)
                        .build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> getOnItemAddedObs() {
        return itemAppService.getOnItemsAddedObs()
                .map(this::toModel);
    }

    @Override
    public Intent createItemDetailsIntent(@NotNull ItemModel item) {
        final Intent intent = new Intent(context, ItemDetailsActivity.class);
        intent.putExtra(ItemDetailsPresenter.ARG_ITEM, item);
        return intent;
    }

    @Override
    public Intent createComplainIntent(@NotNull String email, @NotNull String subject, @NotNull String body) {
        final Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }

    @Override
    public Observable<Collection<ItemModel>> search(@NotNull String query, long limit) {
        return itemAppService
                .fetchItems(new FilterBuilder()
                        .withQuery(query.replaceAll("(\\s\\s\\s*)|(\\n)", " "))
                        .withLimit(limit)
                        .build())
                .map(this::toModel);
    }

    private Collection<ItemModel> toModel(Collection<Item> items) {
        final Collection<ItemModel> result = new ArrayList<>(items.size());

        for (final Item item : items) {

            final String priceSub =
                    item.getItemInfo().getPrice().compareTo(BigDecimal.ZERO) == 0 ?
                            context.getString(R.string.items_price_free) :
                            context.getString(R.string.items_price, item.getItemInfo().getPrice().toPlainString());

            final ItemModel.Builder builder = new ItemModel.Builder(
                    item.getId().getId(),
                    MyTextUtil.capitalize(item.getDetails().getUserInfo().getUsername()),
                    MyTextUtil.capitalize(item.getItemInfo().getName()),
                    MyTextUtil.capitalize(item.getItemInfo().getDescription()),
                    formatTimestamp(item.getDetails().getPublicationDate()),
                    priceSub,
                    toCategories(item.getCategories()),
                    MyTextUtil.capitalize(item.getDetails().getBrand().getName()),
                    MyTextUtil.capitalize(item.getDetails().getRegion().getName()))
                    .addGallery(toGallery(item.getDetails().getPhotos()));

            final MyURL url = item.getDetails().getUserInfo().getAvatar();

            if (url != null) {
                builder.setUserAvatar(ImageUtils.urlBitmap(url));
            }
            result.add(builder.build());
        }
        return result;
    }

    private static Collection<IUrlFutureBitmap> toGallery(Collection<? extends MyURL> src) {
        final Collection<IUrlFutureBitmap> result = new ArrayList<>(src.size());

        for (final MyURL url : src) {
            result.add(ImageUtils.urlBitmap(url));
        }

        return result;
    }

    private static Collection<String> toCategories(Collection<Category> categories) {

        final Collection<String> result = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            result.add(MyTextUtil.capitalize(category.getTitle()));
        }
        return result;
    }

    /**
     * Formats timestamp
     *
     * @param timestamp timestamp to format
     * @return date in format '14 June 2016' or 'n minutes ago' or 'Just now'
     */
    private String formatTimestamp(DateTime timestamp) {

        final DateTime fewMinAgo = DateTime.now().minusMinutes(15);

        if (fewMinAgo.compareTo(timestamp) < 0) {

            final int minDiff = Minutes.minutesBetween(fewMinAgo, timestamp).getMinutes();

            if (minDiff > 0)
                return String.format(context.getString(R.string.time_minutes_ago), minDiff);

            return context.getString(R.string.time_now);
        }
        return timestamp.toString(context.getString(R.string.time_general));
    }

}
