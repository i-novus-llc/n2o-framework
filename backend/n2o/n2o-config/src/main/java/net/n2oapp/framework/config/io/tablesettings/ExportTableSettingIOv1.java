package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.ExportFormatEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oExportTableSetting;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class ExportTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oExportTableSetting> {
    @Override
    public String getElementName() {
        return "export";
    }

    @Override
    public void io(Element e, N2oExportTableSetting m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnumArray(e, "format", ",", m::getFormat, m::setFormat, ExportFormatEnum.class);
        p.attributeEnum(e, "default-format", m::getDefaultFormat, m::setDefaultFormat, ExportFormatEnum.class);
    }

    @Override
    public Class<N2oExportTableSetting> getElementClass() {
        return N2oExportTableSetting.class;
    }
}