package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.api.filter.FilterBuilder;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IParcelableFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;
import com.ua.erent.module.core.util.MyTextUtil;

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

    private final IItemAppService appService;
    private final Context context;

    @Inject
    public ItemsModel(Context context, IItemAppService appService) {
        this.appService = appService;
        this.context = context;
    }

    @Override
    public Observable<Collection<ItemModel>> fetch(long limit) {
        return appService.fetchItems(new FilterBuilder().withLimit(limit).build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(long limit, long lastId) {
        return appService
                .fetchItems(new FilterBuilder().withLastIdGreater(lastId)
                        .sort(FilterBuilder.SortType.ASC).withLimit(limit).build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(long limit, long lastId) {
        return appService
                .fetchItems(new FilterBuilder().withLastIdLower(lastId).withLimit(limit).build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> getOnItemAddedObs() {
        return appService.getOnItemsAddedObs()
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

    private static Collection<IParcelableFutureBitmap> toGallery(Collection<? extends MyURL> src) {
        final Collection<IParcelableFutureBitmap> result = new ArrayList<>(src.size());

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
