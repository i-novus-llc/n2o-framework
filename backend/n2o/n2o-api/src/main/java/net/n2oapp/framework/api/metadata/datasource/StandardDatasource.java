package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

/**
 * Клиентская модель источника данных
 */
@Getter
@Setter
public class StandardDatasource extends AbstractDatasource {

    @JsonProperty
    private ClientDataProvider provider;
    private DefaultValuesModeEnum defaultValuesMode;
    private String queryId;
    @JsonProperty
    private ClientDataProvider submit;
    @JsonProperty
    private Boolean fetchOnInit;
}
