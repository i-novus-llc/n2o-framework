package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;

import java.io.Serializable;

@Getter
@Setter
public abstract class AbstractDatasource implements Compiled, IdAware, Serializable {

    @JsonProperty
    private String id;
    private DefaultValuesMode defaultValuesMode;
}
