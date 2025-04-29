package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Исходная модель Кэширующего источника данных
 */
@Getter
@Setter
public class N2oCachedDatasource extends N2oDatasource implements Submittable {

    private String queryId;
    private String objectId;
    private String route;
    private String storageKey;
    private BrowserStorageTypeEnum storageType;
    private String cacheExpires;
    private N2oPreFilter[] filters;
    private Submit submit;
    private Boolean fetchOnInit;
}
