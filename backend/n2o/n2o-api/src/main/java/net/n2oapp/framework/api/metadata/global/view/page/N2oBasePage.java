package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.ActionBarAware;
import net.n2oapp.framework.api.metadata.aware.ToolbarsAware;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель страницы
 */
@Getter
@Setter
public abstract class N2oBasePage extends N2oPage implements ActionBarAware, ToolbarsAware {
    private String datasourceId;//TODO если потребуется добавить интерфейс DatasourceIdAware, то стоит изменить метод AbstractActionCompiler.getLocalDatasourceId
    private ActionBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;
    private N2oAbstractDatasource[] datasources;
}
