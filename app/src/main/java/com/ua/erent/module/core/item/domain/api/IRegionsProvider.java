package com.ua.erent.module.core.item.domain.api;

import com.ua.erent.module.core.item.domain.vo.Region;

import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 12/6/2016.
 */

public interface IRegionsProvider {

    Observable<Collection<Region>> fetchRegions();

}
