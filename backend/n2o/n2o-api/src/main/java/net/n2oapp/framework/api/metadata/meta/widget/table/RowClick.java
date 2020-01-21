package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Клиентская модель клика по строке
 */
@Getter
@Setter
public class RowClick implements Compiled {
    @JsonProperty
    private Action action;
    @JsonProperty
    private String enablingCondition;

    public RowClick(Action action) {
        this.action = action;
    }
}
