package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель Кэширующего источника данных
 */
@Getter
@Setter
public class CachedDatasource extends AbstractDatasource {

    @JsonProperty
    private Provider provider;
    @JsonProperty
    private Submit submit;
    @JsonProperty
    private Boolean fetchOnInit;

    @Getter
    @Setter
    public static class Provider implements Compiled {
        @JsonProperty
        private String type = "cached";
        @JsonProperty
        private String key;
        @JsonProperty
        private BrowserStorageType storage;
        @JsonProperty
        private String cacheExpires;
        @JsonProperty
        private List<String> invalidateParams;
        @JsonProperty
        private String url;
        @JsonProperty
        private Map<String, ModelLink> pathMapping;
        @JsonProperty
        private Map<String, ModelLink> queryMapping;
        @JsonProperty
        private Integer size;
    }

    @Getter
    @Setter
    public static class Submit extends ClientDataProvider {
        @JsonProperty
        private Boolean clearCache;
        @JsonProperty
        private String type = "cached";
        @JsonProperty
        private String key;
        @JsonProperty
        private ReduxModel model;
        @JsonProperty
        private BrowserStorageType storage;
    }
}
