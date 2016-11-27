package com.ua.erent.module.core.item.domain.api;

import android.net.Uri;

import com.ua.erent.BuildConfig;
import com.ua.erent.module.core.account.auth.user.domain.vo.UserID;
import com.ua.erent.module.core.item.domain.bo.Item;
import com.ua.erent.module.core.item.domain.vo.Brand;
import com.ua.erent.module.core.item.domain.vo.Details;
import com.ua.erent.module.core.item.domain.vo.ItemID;
import com.ua.erent.module.core.item.domain.vo.ItemInfo;
import com.ua.erent.module.core.item.domain.vo.Region;
import com.ua.erent.module.core.item.domain.vo.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import dagger.internal.Preconditions;

/**
 * <p>
 * Converters server api response into {@linkplain Item}
 * </p>
 * Created by Максим on 11/8/2016.
 */

final class ConverterFactory {

    private ConverterFactory() {
        throw new RuntimeException();
    }

    /**
     * Converts {@linkplain ItemResponse} into {@linkplain Item}
     *
     * @param convert server response to convert
     * @return new instance of {@linkplain Item}
     */
    static Item toItem(@NotNull ItemResponse convert) {

        Preconditions.checkNotNull(convert);

        return new Item.Builder().setId(new ItemID(convert.id))
                .setItemInfo(new ItemInfo(convert.name, convert.description, new BigDecimal(convert.price)))
                .setDetails(new Details.Builder()
                        .setBrand(new Brand(convert.brand.id, convert.brand.name, convert.brand.description))
                        .setRegion(new Region(convert.region.id, convert.region.name))
                        .setPublicationDate(convert.publicationDate)
                        .setUserInfo(
                                new UserInfo(new UserID(convert.owner.id), convert.owner.username,
                                        Uri.parse(BuildConfig.API_BASE_URL.concat(convert.owner.avatarUrl)))
                        ).build())
                .build();
    }

    /**
     * Converts collection of {@linkplain ItemResponse} into collection of {@linkplain Item}
     *
     * @param convert server response collection to convert
     * @return new collection of {@linkplain Item}
     */
    static Collection<Item> toItem(@NotNull Collection<ItemResponse> convert) {
        Preconditions.checkNotNull(convert);

        final Collection<Item> result = new ArrayList<>(convert.size());

        for (final ItemResponse respItem : convert) {
            result.add(toItem(respItem));
        }
        return result;
    }

}
