package com.ua.erent.module.core.presentation.mvp.util;

import android.content.Context;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.Brand;
import com.ua.erent.module.core.item.domain.vo.Region;
import com.ua.erent.module.core.presentation.mvp.presenter.model.BrandModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.ItemModel;
import com.ua.erent.module.core.presentation.mvp.presenter.model.RegionModel;
import com.ua.erent.module.core.presentation.mvp.view.util.IUrlFutureBitmap;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;
import com.ua.erent.module.core.presentation.mvp.view.util.MyURL;
import com.ua.erent.module.core.util.MyTextUtil;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 12/6/2016.
 */

public final class ItemConverter {

    private static final Map<Long, Integer> categoryIdToImage;

    static {
        categoryIdToImage = new HashMap<>(5);

        categoryIdToImage.put(1L, R.drawable.clothes_category);
        categoryIdToImage.put(2L, R.drawable.tourism_category);
        categoryIdToImage.put(3L, R.drawable.transport_category);
        categoryIdToImage.put(4L, R.drawable.toys_category);
        categoryIdToImage.put(5L, R.drawable.furniture_category);
    }

    private ItemConverter() {
        throw new RuntimeException();
    }

    public static Collection<ItemModel> toCategoryModel(Context context, Collection<Item> items) {
        final Collection<ItemModel> result = new ArrayList<>(items.size());

        for (final Item item : items) {

            final String priceSub =
                    item.getItemInfo().getPrice().compareTo(BigDecimal.ZERO) == 0 ?
                            context.getString(R.string.items_price_free) :
                            context.getString(R.string.items_price, item.getItemInfo().getPrice().toPlainString());

            final ItemModel.Builder builder = new ItemModel.Builder(
                    item.getId().getId(),
                    item.getDetails().getUserInfo().getId().getId(),
                    MyTextUtil.capitalize(item.getDetails().getUserInfo().getUsername()),
                    MyTextUtil.capitalize(item.getItemInfo().getName()),
                    MyTextUtil.capitalize(item.getItemInfo().getDescription()),
                    formatTimestamp(context, item.getDetails().getPublicationDate()),
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

    public static Collection<BrandModel> toBrandModel(Collection<Brand> from) {
        final Collection<BrandModel> result = new ArrayList<>(from.size());

        for (final Brand brand : from) {
            result.add(new BrandModel(brand.getId(), brand.getName(), brand.getDescription()));
        }
        return result;
    }

    public static Collection<RegionModel> toRegionModel(Collection<Region> from) {
        final Collection<RegionModel> result = new ArrayList<>(from.size());

        for (final Region region : from) {
            result.add(new RegionModel(region.getId(), region.getName()));
        }
        return result;
    }

    public static Collection<CategoryModel> toCategoryModel(Collection<Category> categories) {

        final Collection<CategoryModel> result = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            final long id = category.getId().getId();
            result.add(new CategoryModel(id, category.getTitle(),
                    category.getDescription(),
                    categoryIdToImage.containsKey(id) ? ImageUtils.resourceBitmap(categoryIdToImage.get(id)) : null)
            );
        }

        return result;
    }

    /**
     * Formats timestamp
     *
     * @param timestamp timestamp to format
     * @return date in format '14 June 2016' or 'n minutes ago' or 'Just now'
     */
    private static String formatTimestamp(Context context, DateTime timestamp) {

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
