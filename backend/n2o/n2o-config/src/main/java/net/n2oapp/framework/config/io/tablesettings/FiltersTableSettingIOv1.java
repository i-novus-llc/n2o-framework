package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oFiltersTableSetting;
import org.springframework.stereotype.Component;

@Component
public class FiltersTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oFiltersTableSetting> {
    @Override
    public String getElementName() {
        return "filters";
    }

    @Override
    public Class<N2oFiltersTableSetting> getElementClass() {
        return N2oFiltersTableSetting.class;
    }
}