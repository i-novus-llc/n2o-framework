package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

/**
 * Информация о поллинге после действия
 */
@Getter
@Setter
public class PollingSaga implements Compiled {
    @JsonProperty
    private String delay;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModel model;
    @JsonProperty
    private ClientDataProvider dataProvider;
}
