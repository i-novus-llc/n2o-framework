package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Клиентская модель абстрактной ячейки
 */
@Getter
@Setter
public abstract class AbstractCell implements Cell, JsonPropertiesAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String fieldKey;
    @JsonProperty
    private String src;
    @JsonProperty
    private Object visible;
    private Map<String, Object> properties;
    @JsonProperty
    private String tooltipFieldId;
    @JsonProperty
    private Map<String, Object> elementAttributes = new HashMap<>();
}
