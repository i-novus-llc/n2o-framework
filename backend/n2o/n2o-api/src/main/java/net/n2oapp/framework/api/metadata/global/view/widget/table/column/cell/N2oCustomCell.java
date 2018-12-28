package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import java.util.Map;


/**
 * @author iryabov
 * @since 10.12.2015
 */
@Getter
@Setter
public class N2oCustomCell extends N2oActionCell {
    private N2oSwitch cssClassSwitch;
    private Map<String, Object> properties;
}
