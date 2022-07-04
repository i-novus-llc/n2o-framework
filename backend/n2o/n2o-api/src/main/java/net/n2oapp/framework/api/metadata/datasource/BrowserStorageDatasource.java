package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.Dependency;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель источника, хранящего данные в браузере
 */
@Getter
@Setter
public class BrowserStorageDatasource extends AbstractDatasource {

    @JsonProperty
    private Provider provider;
    @JsonProperty
    private Map<String, List<Validation>> validations;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Submit submit;
    @JsonProperty
    private List<Dependency> dependencies;


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
        private BrowserStorageType storage;
    }
}
