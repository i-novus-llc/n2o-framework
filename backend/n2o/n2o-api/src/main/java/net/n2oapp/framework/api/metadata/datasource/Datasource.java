package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель источника данных
 */
@Getter
@Setter
public class Datasource extends AbstractDatasource {

    @JsonProperty
    private ClientDataProvider provider;
    @JsonProperty
    private Map<String, List<Validation>> validations;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private ClientDataProvider submit;
    @JsonProperty
    private List<DependencyCondition> dependencies;
}
