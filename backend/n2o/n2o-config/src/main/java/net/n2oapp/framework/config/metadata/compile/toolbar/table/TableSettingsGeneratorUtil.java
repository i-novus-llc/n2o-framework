package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.*;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
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

    public static N2oButton generateColumns(N2oToolbar toolbar, CompileProcessor p) {
        N2oButton columnsButton = new N2oButton();
        fillButton(columnsButton, toolbar.getIsGeneratedForSubMenu(), "columns", p);
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
        fillButton(filterButton, toolbar.getIsGeneratedForSubMenu(), "filters", p);
        N2oCustomAction filterAction = new N2oCustomAction();
        filterAction.setType(p.resolve(property("n2o.api.generate.button.filters.action.type"), String.class));
        Map<String, String> payload = Collections.singletonMap("widgetId", widgetId);
        filterAction.setPayload(payload);
        filterButton.setActions(new N2oCustomAction[]{filterAction});
        filterButton.setModel(ReduxModel.filter);
        return filterButton;
    }

    public static N2oButton generateRefresh(N2oToolbar toolbar, CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        fillButton(refreshButton, toolbar.getIsGeneratedForSubMenu(), "refresh", p);
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setActions(new N2oRefreshAction[]{refreshAction});
        refreshButton.setModel(ReduxModel.filter);
        return refreshButton;
    }

    public static N2oButton generateResize(N2oToolbar toolbar, CompileProcessor p) {
        N2oButton resizeButton = new N2oButton();
        fillButton(resizeButton, toolbar.getIsGeneratedForSubMenu(), "resize", p);
        resizeButton.setSrc(p.resolve(property("n2o.api.generate.button.resize.action.src"), String.class));
        resizeButton.setModel(ReduxModel.filter);
        return resizeButton;
    }

    public static N2oButton generateWordWrap(N2oToolbar toolbar, CompileProcessor p) {
        N2oButton wordWrapButton = new N2oButton();
        fillButton(wordWrapButton, toolbar.getIsGeneratedForSubMenu(), "wordwrap", p);
        N2oCustomAction wordWrapAction = new N2oCustomAction();

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        Map<String, String> payload = Collections.singletonMap("widgetId", widgetScope.getClientWidgetId());

        wordWrapButton.setSrc(p.resolve(property("n2o.api.generate.button.wordwrap.action.src"), String.class));
        wordWrapAction.setType(p.resolve(property("n2o.api.generate.button.wordwrap.action.type"), String.class));
        wordWrapAction.setPayload(payload);
        wordWrapButton.setActions(new N2oCustomAction[]{wordWrapAction});
        wordWrapButton.setModel(ReduxModel.filter);

        return wordWrapButton;
    }

    public static N2oButton generateExport(N2oToolbar toolbar, CompileProcessor p) {
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

        N2oButton downloadBtn = new N2oButton();
        downloadBtn.setLabel("Загрузить");
        downloadBtn.setIcon("fa fa-download");
        downloadBtn.setColor("primary");
        N2oCustomAction downloadAction = new N2oCustomAction();
        Map<String, String> payload = new HashMap<>();
        payload.put("baseURL", "/n2o/export");
        payload.put("exportDatasource", exportDatasource);
        payload.put("configDatasource", configDatasource);
        downloadAction.setPayload(payload);
        downloadAction.setType("n2o/api/utils/export");
        downloadBtn.setActions(new N2oAction[]{downloadAction});

        N2oButton closeBtn = new N2oButton();
        closeBtn.setLabel("Закрыть");
        N2oCloseAction closeAction = new N2oCloseAction();
        closeBtn.setActions(new N2oAction[]{closeAction});

        N2oToolbar modalToolbar = new N2oToolbar();
        modalToolbar.setPlace("bottomRight");
        modalToolbar.setItems(new ToolbarItem[]{downloadBtn, closeBtn});

        N2oShowModal showModalAction = new N2oShowModal();
        showModalAction.setToolbars(new N2oToolbar[]{modalToolbar});
        showModalAction.setPageId(p.resolve(property("n2o.api.generate.button.export.page"), String.class));
        showModalAction.setRoute("/exportModal");

        N2oButton exportButton = new N2oButton();
        fillButton(exportButton, toolbar.getIsGeneratedForSubMenu(), "export", p);
        exportButton.setActions(new N2oShowModal[]{showModalAction});
        exportButton.setModel(ReduxModel.filter);

        return exportButton;
    }

    private static void fillButton(N2oButton button, Boolean isForSubMenu,
                                   String key, CompileProcessor p) {
        String label = p.getMessage(String.format("n2o.api.generate.button.%s.description", key));

        if (Boolean.TRUE.equals(isForSubMenu))
            button.setLabel(label);
        else {
            button.setDescription(label);
            button.setIcon(p.resolve(property(String.format("n2o.api.generate.button.%s.icon", key)), String.class));
        }
    }
}
