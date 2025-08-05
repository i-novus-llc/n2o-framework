package net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oColumnsTableSetting extends N2oAbstractTableSetting {
    private String[] defaultValue;
    private String[] locked;
}
