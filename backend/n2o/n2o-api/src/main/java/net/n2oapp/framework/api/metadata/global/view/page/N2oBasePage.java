package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.aware.ActionBarAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ToolbarsAware;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель страницы
 */
@Getter
@Setter
public abstract class N2oBasePage extends N2oPage implements ActionBarAware, ToolbarsAware, DatasourceIdAware {
    private String datasourceId;
    private ActionBar[] actions;
    private GenerateType actionGenerate;
    @N2oAttribute("Список меню с кнопками")
    private N2oToolbar[] toolbars;
    private N2oAbstractDatasource[] datasources;
    private N2oAbstractEvent[] events;
}
