package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Исходная модель Кэширующего источника данных
 */
@Getter
@Setter
public class N2oCachedDatasource extends N2oAbstractDatasource implements Submittable {

    private String queryId;
    private String objectId;
    private String route;
    private String storageKey;
    private BrowserStorageType storageType;
    private String cacheExpires;
    private String[] invalidateCachePathParams;
    private String[] invalidateCacheQueryParams;
    private N2oPreFilter[] filters;
    private Submit submit;
}
