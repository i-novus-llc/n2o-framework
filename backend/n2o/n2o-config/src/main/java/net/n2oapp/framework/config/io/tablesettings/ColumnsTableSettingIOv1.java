package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oColumnsTableSetting;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class ColumnsTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oColumnsTableSetting> {

    @Override
    public String getElementName() {
        return "columns";
    }

    @Override
    public void io(Element e, N2oColumnsTableSetting m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "default-value", m::getDefaultValue, m::setDefaultValue);
        p.attribute(e, "locked", m::getLocked, m::setLocked);
    }

    @Override
    public Class<N2oColumnsTableSetting> getElementClass() {
        return N2oColumnsTableSetting.class;
    }
}