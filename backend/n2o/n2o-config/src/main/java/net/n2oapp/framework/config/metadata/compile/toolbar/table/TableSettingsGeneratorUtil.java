package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
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
        columnsButton.setDescription(resolveDescription(p, "columns"));
        String icon = resolveIcon(p, "columns");
        if (icon == null)
            icon = "fa fa-table";
        columnsButton.setIcon(icon);
        columnsButton.setSrc(resolveSrc(p, "columns"));
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
        filterButton.setDescription(resolveDescription(p, "filters"));
        String icon = resolveIcon(p, "filters");
        if (icon == null)
            icon = "fa fa-filter";
        filterButton.setIcon(icon);
        N2oCustomAction filterAction = new N2oCustomAction();
        filterAction.setType(resolveActionType(p, "filters"));
        Map<String, String> payload = Collections.singletonMap("widgetId", widgetId);
        filterAction.setPayload(payload);
        filterButton.setActions(new N2oCustomAction[]{filterAction});
        filterButton.setModel(ReduxModel.filter);
        return filterButton;
    }

    public static N2oButton generateRefresh(CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        refreshButton.setDescription(resolveDescription(p, "refresh"));
        String icon = resolveIcon(p, "refresh");
        if (icon == null)
            icon = "fa fa-refresh";
        refreshButton.setIcon(icon);
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setActions(new N2oRefreshAction[]{refreshAction});
        refreshButton.setModel(ReduxModel.filter);
        return refreshButton;
    }

    public static N2oButton generateResize(CompileProcessor p) {
        N2oButton resizeButton = new N2oButton();
        resizeButton.setDescription(resolveDescription(p, "resize"));
        String icon = resolveIcon(p, "resize");
        if (icon == null)
            icon = "fa fa-bars";
        resizeButton.setIcon(icon);
        resizeButton.setSrc(resolveSrc(p, "resize"));
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

        wordWrapButton.setDescription(resolveDescription(p, "wordwrap"));
        String icon = resolveIcon(p, "wordwrap");
        if (icon == null)
            icon = "fa-solid fa-grip-lines";
        wordWrapButton.setIcon(icon);
        wordWrapButton.setSrc(resolveSrc(p, "wordwrap"));
        wordWrapAction.setType(resolveActionType(p, "wordwrap"));
        wordWrapAction.setPayload(payload);
        wordWrapButton.setActions(new N2oCustomAction[]{wordWrapAction});
        wordWrapButton.setModel(ReduxModel.filter);

        return wordWrapButton;
    }

    private static String resolveIcon(CompileProcessor p, String generatorType) {
        String icon;
        try {
            icon = p.resolve(property(String.format("n2o.api.generate.button.%s.icon", generatorType)), String.class);
        } catch (Exception e) {
            icon = null;
        }
        return icon;
    }

    private static String resolveDescription(CompileProcessor p, String generatorType) {
        String description;
        try {
            description = p.resolve(property(String.format("n2o.api.generate.button.%s.description", generatorType)), String.class);
        } catch (Exception e) {
            description = p.getMessage(String.format("n2o.api.generate.button.%s.description", generatorType));
        }
        return description;
    }

    private static String  resolveSrc(CompileProcessor p, String generatorType) {
        String src;
        try {
            src = p.resolve(property(String.format("n2o.api.generate.button.%s.action.src", generatorType)), String.class);
        } catch (Exception e) {
            src = p.resolve(property(String.format("n2o.api.generate.button.%s.action.src", generatorType)), String.class);
        }
        return src;
    }

    private static String  resolveActionType(CompileProcessor p, String generatorType) {
        String type;
        try {
            type = p.resolve(property(String.format("n2o.api.generate.button.%s.action.type", generatorType)), String.class);
        } catch (Exception e) {
            type = p.resolve(property(String.format("n2o.api.generate.button.%s.action.type", generatorType)), String.class);
        }
        return type;
    }
}
