package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

import java.util.Map;


/**
 * Настраиваемая ячейка
 */
@Getter
@Setter
public class N2oCustomCell extends N2oActionCell implements JsonPropertiesAware {
    private N2oSwitch cssClassSwitch;
    private Map<String, Object> properties;
}
