package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.script.ScriptProcessor;

/**
 * Ячейка таблицы с checkbox
 */
@Getter
@Setter
public class N2oCheckboxCell extends N2oActionCell {
    private String enabled;

    @JsonProperty
    public Object getDisabled() {
        return ScriptProcessor.invertExpression(enabled);
    }
}
