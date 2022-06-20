package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;


/**
 * Исходная модель источника, хранящего данные в локальном или сессионном хранилище браузера
 */
@Getter
@Setter
public class N2oBrowserStorageDatasource extends N2oDatasource {

    private String key;
    private BrowserStorageType storageType;
    private Submit submit;

    /**
     * Тип хранилища
     */
    @Getter
    public enum BrowserStorageType {
        sessionStorage, localStorage
    }

    @Setter
    @Getter
    public static class Submit implements Source {
        private String key;
        private BrowserStorageType storageType;
        private Boolean auto;
    }
}
