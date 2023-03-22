package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.Collections;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Внутренняя утилита для генерации кнопок таблицы
 */
public class TableSettingsGeneratorUtil {

    public static N2oButton generateColumns(CompileProcessor p) {
        N2oButton columnsButton = new N2oButton();
        columnsButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.columns.description"));
        columnsButton.setIcon("fa fa-table");
        columnsButton.setSrc(p.resolve(property("n2o.api.action.columns.src"), String.class));
        columnsButton.setModel(ReduxModel.filter);
        return columnsButton;
    }

    public static N2oButton generateFilters(N2oToolbar toolbar, CompileProcessor p) {
        String widgetId = toolbar.getTargetWidgetId();
        if (widgetId == null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            widgetId = widgetScope == null ? null : widgetScope.getClientWidgetId();
        }
        N2oButton filterButton = new N2oButton();
        filterButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.filter.description"));
        filterButton.setIcon("fa fa-filter");
        N2oCustomAction filterAction = new N2oCustomAction();
        filterAction.setType(p.resolve(property("n2o.api.action.filters.type"), String.class));
        Map<String, String> payload = Collections.singletonMap("widgetId", widgetId);
        filterAction.setPayload(payload);
        filterButton.setActions(new N2oCustomAction[]{filterAction});
        filterButton.setModel(ReduxModel.filter);
        return filterButton;
    }

    public static N2oButton generateRefresh(CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        refreshButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.refresh.description"));
        refreshButton.setIcon("fa fa-refresh");
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setActions(new N2oRefreshAction[]{refreshAction});
        refreshButton.setModel(ReduxModel.filter);
        return refreshButton;
    }

    public static N2oButton generateResize(CompileProcessor p) {
        N2oButton resizeButton = new N2oButton();
        resizeButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.resize.description"));
        resizeButton.setIcon("fa fa-bars");
        resizeButton.setSrc(p.resolve(property("n2o.api.action.resize.src"), String.class));
        resizeButton.setModel(ReduxModel.filter);
        return resizeButton;
    }

    public static N2oButton generateExport(CompileProcessor p) {
        N2oButton exportButton = new N2oButton();
        N2oShowModal showModalAction = new N2oShowModal();

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String datasourceId = widgetScope == null ? null : widgetScope.getClientDatasourceId();

        showModalAction.setPageId("exportModal");
        showModalAction.setRoute("/:datasourceId/exportTable");
        N2oPathParam n2oPathParam = new N2oPathParam();
        n2oPathParam.setName("datasourceId");
        n2oPathParam.setValue(datasourceId);
        showModalAction.setParams(new N2oParam[]{n2oPathParam});

        exportButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.export.description"));
        exportButton.setIcon("fa-solid fa-arrow-up-from-bracket");
        exportButton.setActions(new N2oShowModal[]{showModalAction});
        exportButton.setModel(ReduxModel.filter);

        return exportButton;
    }
}
