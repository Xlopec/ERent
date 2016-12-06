package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.vo.Brand;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/6/2016.
 */

public interface IBrandsProvider {

    Observable<Collection<Brand>> fetchBrands();

}
