package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.Map;

/**
 * Клиентская модель клика по строке
 */
@Getter
@Setter
public class RowClick implements Compiled, JsonPropertiesAware {
    @JsonProperty
    private Action action;
    @JsonProperty
    private String enablingCondition;
    private Map<String, Object> properties;

    public RowClick(Action action) {
        this.action = action;
    }
}
