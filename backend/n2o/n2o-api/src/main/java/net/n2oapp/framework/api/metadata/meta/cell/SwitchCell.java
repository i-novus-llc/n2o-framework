package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;

import java.util.HashMap;
import java.util.Map;

/**
 * Клиентская модель переключателя ячеек таблицы
 */
@Getter
@Setter
public class SwitchCell extends N2oAbstractCell {
    @JsonProperty
    private String switchFieldId;
    @JsonProperty
    private Map<String, N2oAbstractCell> switchList = new HashMap<>();
    @JsonProperty
    private N2oAbstractCell switchDefault;
}
