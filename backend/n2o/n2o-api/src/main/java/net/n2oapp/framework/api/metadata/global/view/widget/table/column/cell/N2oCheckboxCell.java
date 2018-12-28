package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;

/**
 * Ячейка таблицы с checkbox
 */
@Getter
@Setter
public class N2oCheckboxCell extends N2oActionCell {
    private N2oFieldCondition enablingCondition;
    private Boolean enabled;

    @JsonProperty
    public Boolean getReadOnly() {
        return enabled != null ? !enabled : null;
    }
}
