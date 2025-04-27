package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oRefreshTableSetting;
import org.springframework.stereotype.Component;

@Component
public class RefreshTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oRefreshTableSetting> {
    @Override
    public String getElementName() {
        return "refresh";
    }

    @Override
    public Class<N2oRefreshTableSetting> getElementClass() {
        return N2oRefreshTableSetting.class;
    }
}