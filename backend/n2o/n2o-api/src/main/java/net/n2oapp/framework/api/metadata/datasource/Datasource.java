package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDependency;

import java.util.List;

@Getter
@Setter
public class Datasource implements Compiled {
    @JsonProperty
    private ClientDataProvider provider;
    @JsonProperty
    private List<Validation> validations;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private ClientDataProvider submit;
    @JsonProperty
    private WidgetDependency dependencies;
    private DefaultValuesMode defaultValuesMode;
}
