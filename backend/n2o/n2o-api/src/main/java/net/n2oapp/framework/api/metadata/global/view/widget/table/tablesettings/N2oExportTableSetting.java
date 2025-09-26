package net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oExportTableSetting extends N2oAbstractTableSetting {
    private ExportFormatEnum[] format;
    private ExportFormatEnum defaultFormat;
}