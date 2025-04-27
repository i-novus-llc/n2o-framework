package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResetTableSetting;
import org.springframework.stereotype.Component;

@Component
public class ResetTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oResetTableSetting> {
    @Override
    public String getElementName() {
        return "reset-settings";
    }

    @Override
    public Class<N2oResetTableSetting> getElementClass() {
        return N2oResetTableSetting.class;
    }
}