package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.datasource.Submittable;


/**
 * Исходная модель источника, хранящего данные в браузере
 */
@Getter
@Setter
public class N2oBrowserStorageDatasource extends N2oDatasource implements Submittable {

    private String key;
    private BrowserStorageType storageType;
    private Submit submit;
    private Boolean fetchOnInit;

    @Setter
    @Getter
    public static class Submit implements Source {
        private String key;
        private BrowserStorageType storageType;
        private Boolean auto;
        private ReduxModel model;
    }
}
