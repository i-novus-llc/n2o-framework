package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.DatasourceUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Внутренняя утилита для генерации кнопок таблицы
 */
public class TableSettingsGeneratorUtil {

    public static N2oButton generateColumns(CompileProcessor p) {
        N2oButton columnsButton = new N2oButton();
        columnsButton.setDescription(p.getMessage("n2o.api.generate.button.columns.description"));
        columnsButton.setIcon(p.resolve(property("n2o.api.generate.button.columns.icon"), String.class));
        columnsButton.setSrc(p.resolve(property("n2o.api.generate.button.columns.action.src"), String.class));
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
        filterButton.setDescription(p.getMessage("n2o.api.generate.button.filters.description"));
        filterButton.setIcon(p.resolve(property("n2o.api.generate.button.filters.icon"), String.class));
        N2oCustomAction filterAction = new N2oCustomAction();
        filterAction.setType(p.resolve(property("n2o.api.generate.button.filters.action.type"), String.class));
        Map<String, String> payload = Collections.singletonMap("widgetId", widgetId);
        filterAction.setPayload(payload);
        filterButton.setActions(new N2oCustomAction[]{filterAction});
        filterButton.setModel(ReduxModel.filter);
        return filterButton;
    }

    public static N2oButton generateRefresh(CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        refreshButton.setDescription(p.getMessage("n2o.api.generate.button.refresh.description"));
        refreshButton.setIcon(p.resolve(property("n2o.api.generate.button.refresh.icon"), String.class));
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setActions(new N2oRefreshAction[]{refreshAction});
        refreshButton.setModel(ReduxModel.filter);
        return refreshButton;
    }

    public static N2oButton generateResize(CompileProcessor p) {
        N2oButton resizeButton = new N2oButton();
        resizeButton.setDescription(p.getMessage("n2o.api.generate.button.resize.description"));
        resizeButton.setIcon(p.resolve(property("n2o.api.generate.button.resize.icon"), String.class));
        resizeButton.setSrc(p.resolve(property("n2o.api.generate.button.resize.action.src"), String.class));
        resizeButton.setModel(ReduxModel.filter);
        return resizeButton;
    }

    public static N2oButton generateWordWrap(N2oToolbar toolbar, CompileProcessor p) {
        N2oButton wordWrapButton = new N2oButton();
        N2oCustomAction wordWrapAction = new N2oCustomAction();

        String datasourceId = toolbar.getDatasourceId();
        if (datasourceId == null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            datasourceId = widgetScope == null ? null : widgetScope.getDatasourceId();
        }
        Map<String, String> payload = Collections.singletonMap("datasource", datasourceId);

        wordWrapButton.setDescription(p.getMessage("n2o.api.generate.button.wordwrap.description"));
        wordWrapButton.setIcon(p.resolve(property("n2o.api.generate.button.wordwrap.icon"), String.class));
        wordWrapButton.setSrc(p.resolve(property("n2o.api.generate.button.wordwrap.action.src"), String.class));
        wordWrapAction.setType(p.resolve(property("n2o.api.generate.button.wordwrap.action.type"), String.class));
        wordWrapAction.setPayload(payload);
        wordWrapButton.setActions(new N2oCustomAction[]{wordWrapAction});
        wordWrapButton.setModel(ReduxModel.filter);

        return wordWrapButton;
    }

    public static N2oButton generateExport(CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String datasourceId = widgetScope == null ? null : widgetScope.getDatasourceId();

        String configDatasource = DatasourceUtil.getClientDatasourceId(
                "exportModalDs",
                "exportModal",
                p);
        String exportDatasource = DatasourceUtil.getClientDatasourceId(
                datasourceId,
                p.getScope(PageScope.class).getPageId(),
                p);

        N2oButton uploadBtn = new N2oButton();
        uploadBtn.setLabel("Загрузить");
        uploadBtn.setIcon("fa fa-download");
        uploadBtn.setColor("primary");
        N2oCustomAction uploadAction = new N2oCustomAction();
        Map<String, String> payload = new HashMap<>();
        payload.put("baseURL", "/n2o/export");
        payload.put("exportDatasource", exportDatasource);
        uploadAction.setPayload(payload);
        uploadAction.setType("n2o/api/utils/export");
        uploadBtn.setActions(new N2oAction[]{uploadAction});

        N2oButton closeBtn = new N2oButton();
        closeBtn.setLabel("Закрыть");
        N2oCloseAction closeAction = new N2oCloseAction();
        closeBtn.setActions(new N2oAction[]{closeAction});

        N2oToolbar toolbar = new N2oToolbar();
        toolbar.setPlace("bottomRight");
        toolbar.setItems(new ToolbarItem[]{uploadBtn, closeBtn});

        N2oStandardDatasource exportModalDS = new N2oStandardDatasource();
        exportModalDS.setId(configDatasource);

        N2oShowModal showModalAction = new N2oShowModal();
        showModalAction.setDatasources(new N2oAbstractDatasource[]{exportModalDS});
        showModalAction.setToolbars(new N2oToolbar[]{toolbar});
        showModalAction.setPageId(p.resolve(property("n2o.api.generate.button.export.page"), String.class));
        showModalAction.setRoute("/exportTable/exportTable");

        N2oButton exportButton = new N2oButton();
        exportButton.setDescription(p.getMessage("n2o.api.generate.button.export.description"));
        exportButton.setIcon(p.resolve(property("n2o.api.generate.button.export.icon"), String.class));
        exportButton.setActions(new N2oShowModal[]{showModalAction});
        exportButton.setModel(ReduxModel.filter);

        return exportButton;
    }
}
