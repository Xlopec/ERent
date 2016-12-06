package com.ua.erent.module.core.presentation.mvp.util;

import com.ua.erent.R;
import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.presentation.mvp.presenter.model.CategoryModel;
import com.ua.erent.module.core.presentation.mvp.view.util.ImageUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 12/5/2016.
 */

public final class CategoriesConverter {

    private static final Map<Long, Integer> categoryIdToImage;

    static {
        categoryIdToImage = new HashMap<>(5);

        categoryIdToImage.put(1L, R.drawable.clothes_category);
        categoryIdToImage.put(2L, R.drawable.tourism_category);
        categoryIdToImage.put(3L, R.drawable.transport_category);
        categoryIdToImage.put(4L, R.drawable.toys_category);
        categoryIdToImage.put(5L, R.drawable.furniture_category);
    }

    private CategoriesConverter() {
        throw new RuntimeException();
    }

    public static Collection<CategoryModel> toModel(Collection<Category> categories) {

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

}
