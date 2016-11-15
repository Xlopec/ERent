package com.ua.erent.module.core.item.domain.storage;

import com.ua.erent.module.core.item.domain.bo.Category;
import com.ua.erent.module.core.item.domain.vo.CategoryID;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Максим on 11/15/2016.
 */

final class CategoryMapper {

    private CategoryMapper() {
    }

    static Collection<CategoryPO> toPo(Collection<Category> categories) {

        final Collection<CategoryPO> categoryPOs = new ArrayList<>(categories.size());

        for (final Category category : categories) {
            categoryPOs.add(new CategoryPO(category.getId().getId(), category.getTitle(),
                    category.getDescription()));
        }

        return categoryPOs;
    }

    static Collection<Category> fromPo(Collection<CategoryPO> categoryPOs) {

        final Collection<Category> categories = new ArrayList<>(categoryPOs.size());

        for (final CategoryPO categoryPo : categoryPOs) {
            categories.add(new Category(categoryPo.getTitle(), new CategoryID(categoryPo.getId()),
                    categoryPo.getDescription()));
        }

        return categories;
    }

}
