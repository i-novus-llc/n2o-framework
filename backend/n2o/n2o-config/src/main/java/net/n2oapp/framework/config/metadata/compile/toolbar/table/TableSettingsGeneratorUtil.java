package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.*;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelTypeEnum;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableSettingsGeneratorUtil {

    private static final String WIDGET_ID = "widgetId";

    public static N2oButton generateColumns(boolean isForSubMenu, CompileProcessor p) {
        N2oButton columnsButton = new N2oButton();
        fillButton(columnsButton, isForSubMenu, "columns", p);
        columnsButton.setSrc(p.resolve(property("n2o.api.generate.button.columns.action.src"), String.class));
        columnsButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        return columnsButton;
    }

    public static N2oButton generateFilters(boolean isForSubMenu, CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String widgetId = widgetScope == null ? null : widgetScope.getClientWidgetId();
        N2oButton filterButton = new N2oButton();
        fillButton(filterButton, isForSubMenu, "filters", p);
        N2oCustomAction filterAction = new N2oCustomAction();
        filterAction.setType(p.resolve(property("n2o.api.generate.button.filters.action.type"), String.class));
        Map<String, String> payload = Collections.singletonMap(WIDGET_ID, widgetId);
        filterAction.setPayload(payload);
        filterButton.setActions(new N2oCustomAction[]{filterAction});
        filterButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        return filterButton;
    }

    public static N2oButton generateRefresh(boolean isForSubMenu, CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        fillButton(refreshButton, isForSubMenu, "refresh", p);
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setActions(new N2oRefreshAction[]{refreshAction});
        refreshButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        return refreshButton;
    }

    public static N2oButton generateResize(boolean isForSubMenu, CompileProcessor p) {
        N2oButton resizeButton = new N2oButton();
        fillButton(resizeButton, isForSubMenu, "resize", p);
        resizeButton.setSrc(p.resolve(property("n2o.api.generate.button.resize.action.src"), String.class));
        resizeButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        return resizeButton;
    }

    public static N2oButton generateWordWrap(boolean isForSubMenu, CompileProcessor p) {
        N2oButton wordWrapButton = new N2oButton();
        fillButton(wordWrapButton, isForSubMenu, "wordwrap", p);
        N2oCustomAction wordWrapAction = new N2oCustomAction();

        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        Map<String, String> payload = new HashMap<>();
        payload.put(WIDGET_ID, widgetScope.getClientWidgetId());
        payload.put("paramKey", p.resolve(property("n2o.api.generate.button.wordwrap.action.param_key"), String.class));

        wordWrapButton.setSrc(p.resolve(property("n2o.api.generate.button.wordwrap.action.src"), String.class));
        wordWrapAction.setType(p.resolve(property("n2o.api.generate.button.wordwrap.action.type"), String.class));
        wordWrapAction.setPayload(payload);
        wordWrapButton.setActions(new N2oCustomAction[]{wordWrapAction});
        wordWrapButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);

        return wordWrapButton;
    }

    public static N2oButton generateExport(boolean isForSubMenu, CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String datasourceId = widgetScope == null ? null : widgetScope.getDatasourceId();
        String clientWidgetId = widgetScope == null ? null : widgetScope.getClientWidgetId();
        String pageId = p.getScope(PageScope.class).getPageId();
        String exportPage = p.resolve(property("n2o.api.generate.button.export.page"), String.class);
        String exportUrl = p.resolve(property("n2o.api.generate.button.export.url"), String.class);
        String allLimit = p.resolve(property("n2o.api.generate.button.export.all_limit"), String.class);
        String configDatasource = DatasourceUtil.getClientDatasourceId(
                "exportModalDs",
                (pageId.equals("_") ? exportPage : pageId + "_" + exportPage) + "_" + clientWidgetId,
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
        payload.put("baseURL", exportUrl);
        payload.put("exportDatasource", exportDatasource);
        payload.put(WIDGET_ID, clientWidgetId);
        payload.put("configDatasource", configDatasource);
        payload.put("allLimit", allLimit);
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
        showModalAction.setPageId(exportPage);
        showModalAction.setRoute("/" + exportPage + "_" + clientWidgetId);

        N2oButton exportButton = new N2oButton();
        fillButton(exportButton, isForSubMenu, "export", p);
        exportButton.setActions(new N2oShowModal[]{showModalAction});
        exportButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        N2oButton.EnablingDependency dependency = new N2oButton.EnablingDependency();
        dependency.setMessage("Недоступно при пустых данных");
        dependency.setValue("this.length > 0");
        dependency.setDatasource(datasourceId);
        dependency.setModel(ReduxModelEnum.DATASOURCE);
        exportButton.setDependencies(new N2oButton.Dependency[]{dependency});

        return exportButton;
    }

    public static N2oButton generateReset(boolean isForSubMenu, CompileProcessor p) {
        N2oButton resetButton = new N2oButton();
        fillButton(resetButton, isForSubMenu, "reset", p);
        resetButton.setSrc(p.resolve(property("n2o.api.generate.button.reset.action.src"), String.class));
        resetButton.setDisableOnEmptyModel(DisableOnEmptyModelTypeEnum.FALSE);
        return resetButton;
    }

    private static void fillButton(N2oButton button, boolean isForSubMenu,
                                   String key, CompileProcessor p) {
        String label = p.getMessage(String.format("n2o.api.generate.button.%s.description", key));

        if (isForSubMenu)
            button.setLabel(label);
        else {
            button.setDescription(label);
            button.setIcon(p.resolve(property(String.format("n2o.api.generate.button.%s.icon", key)), String.class));
        }
    }
}
