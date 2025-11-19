package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oResizeTableSetting;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class ResizeTableSettingIOv1 extends AbstractTableSettingsIOv1<N2oResizeTableSetting> {

    @Override
    public String getElementName() {
        return "resize";
    }

    @Override
    public void io(Element e, N2oResizeTableSetting m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeArray(e, "size", ",", m::getSize, m::setSize);
    }

    @Override
    public Class<N2oResizeTableSetting> getElementClass() {
        return N2oResizeTableSetting.class;
    }
}