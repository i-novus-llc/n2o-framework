package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;

/**
 * Клиентская модель источника, хранящего данные в браузере
 */
@Getter
@Setter
public class BrowserStorageDatasource extends AbstractDatasource {

    @JsonProperty
    private Provider provider;
    @JsonProperty
    private Submit submit;

    @Getter
    @Setter
    public static class Provider {
        @JsonProperty
        private String type = "browser";
        @JsonProperty
        private String key;
        @JsonProperty
        private BrowserStorageType storage;
    }

    @Getter
    @Setter
    public static class Submit {
        @JsonProperty
        private String type = "browser";
        @JsonProperty
        private String key;
        @JsonProperty
        private Boolean auto;
        @JsonProperty
        private ReduxModel model;
        @JsonProperty
        private BrowserStorageType storage;
    }
}
