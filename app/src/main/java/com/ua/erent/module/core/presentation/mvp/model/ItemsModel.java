package com.ua.erent.module.core.presentation.mvp.model;

import android.content.Context;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.IItemAppService;
import com.ua.erent.module.core.item.domain.api.filter.FilterBuilder;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.presentation.mvp.model.interfaces.IItemsModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

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
        return appService.fetchItems(new FilterBuilder()/*.withLimit(limit)*/.build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchNext(long limit, long lastId) {
        return appService
                .fetchItems(new FilterBuilder().withLastId(lastId).withLimit(limit).build())
                .map(this::toModel);
    }

    @Override
    public Observable<Collection<ItemModel>> fetchPrev(long limit, int offset) {
        return appService
                .fetchItems(new FilterBuilder().withOffset(offset).withLimit(limit).build())
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
            final ItemModel.Builder builder = new ItemModel.Builder(
                    item.getId().getId(),
                    item.getDetails().getUserInfo().getUsername(),
                    item.getItemInfo().getName(),
                    item.getItemInfo().getDescription(),
                    formatTimestamp(item.getDetails().getPublicationDate()),
                    item.getItemInfo().getPrice().toPlainString(),
                    toCategories(item.getCategories()),
                    item.getDetails().getBrand().getName(),
                    item.getDetails().getRegion().getName());

            final MyURL url = item.getDetails().getUserInfo().getAvatar();

            if (url != null) {
                builder.setUserAvatar(ImageUtils.urlBitmap(url));
            }

            result.add(builder.build());
        }

        return result;
    }

    private static Collection<String> toCategories(Collection<Category> categories) {

        final Collection<String> result = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            result.add(category.getTitle());
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
