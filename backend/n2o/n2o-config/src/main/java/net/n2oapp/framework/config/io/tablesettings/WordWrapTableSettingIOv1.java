package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oWordWrapTableSetting;
import org.springframework.stereotype.Component;

@Component
public class WordWrapTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oWordWrapTableSetting> {

    @Override
    public String getElementName() {
        return "word-wrap";
    }

    @Override
    public Class<N2oWordWrapTableSetting> getElementClass() {
        return N2oWordWrapTableSetting.class;
    }
}