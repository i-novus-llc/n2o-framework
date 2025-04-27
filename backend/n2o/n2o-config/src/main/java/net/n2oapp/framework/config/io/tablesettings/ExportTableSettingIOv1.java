package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oExportTableSetting;
import org.springframework.stereotype.Component;

@Component
public class ExportTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oExportTableSetting> {
    @Override
    public String getElementName() {
        return "export";
    }

    @Override
    public Class<N2oExportTableSetting> getElementClass() {
        return N2oExportTableSetting.class;
    }
}