package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

@Getter
@Setter
public class N2oBrowserStorageDatasource extends N2oDatasource {

    private String key;
    private BrowserStorageType storageType;
    private Submit submit;

    @Getter
    public enum BrowserStorageType implements Source {
        sessionStorage, localStorage
    }

    @Setter
    @Getter
    public static class Submit {
        private Boolean auto;
        private BrowserStorageType storageType;
        private String key;
    }
}
