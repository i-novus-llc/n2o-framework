package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oAbstractTableSetting;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;

/**
 * Чтение/запись пользовательских настроек отображения таблицы
 */
public abstract class AbstractTableSettingsIOv1<T extends N2oAbstractTableSetting> extends ComponentIO<T> implements TableSettingsIOv1 {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "label", m::getLabel, m::setLabel);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "description", m::getDescription, m::setDescription);
    }
}