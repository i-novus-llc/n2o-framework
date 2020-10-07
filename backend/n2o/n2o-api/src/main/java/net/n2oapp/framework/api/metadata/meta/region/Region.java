package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель региона n2o
 */
@Getter
@Setter
public abstract class Region implements Compiled, SrcAware, IdAware, JsonPropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String src;
    @JsonProperty
    private List<Compiled> content;
    private Map<String, Object> properties;
}
