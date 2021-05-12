package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

import java.util.List;

/**
 * Клиентская модель региона n2o
 */
@Getter
@Setter
public abstract class Region extends Component implements SrcAware, IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private List<Compiled> content;
}
