package net.n2oapp.framework.config.io.tablesettings;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings.N2oAbstractTableSetting;
import org.jdom2.Namespace;

/**
 * Интерфейс пользовательских настроек отображения таблицы
 */

public interface TableSettingsIOv1 extends NamespaceUriAware, BaseElementClassAware<N2oAbstractTableSetting> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/table-settings-1.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oAbstractTableSetting> getBaseElementClass() {
        return N2oAbstractTableSetting.class;
    }
}
