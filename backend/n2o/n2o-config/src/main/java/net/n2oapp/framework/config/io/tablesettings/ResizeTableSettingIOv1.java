package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResizeTableSetting;
import org.springframework.stereotype.Component;

@Component
public class ResizeTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oResizeTableSetting> {

    @Override
    public String getElementName() {
        return "resize";
    }

    @Override
    public Class<N2oResizeTableSetting> getElementClass() {
        return N2oResizeTableSetting.class;
    }
}