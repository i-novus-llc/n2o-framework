package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

import java.util.Map;

/**
 * Абстрактная реализация клиентской модели действия
 */
@Getter
@Setter
public abstract class AbstractAction<O extends ActionOptions> implements Action {
    @JsonProperty
    private String id;
    @JsonProperty
    private String src;
    @JsonProperty
    private O options;
    @JsonProperty
    private String enabledCondition;
    //for PropertiesAware
    private Map<String, Object> properties;


    public AbstractAction(O options) {
        this.options = options;
    }
}
