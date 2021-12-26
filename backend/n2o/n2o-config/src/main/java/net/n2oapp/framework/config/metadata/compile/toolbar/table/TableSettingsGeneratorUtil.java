package net.n2oapp.framework.config.metadata.compile.toolbar.table;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oPerform;
import net.n2oapp.framework.api.metadata.event.action.N2oRefreshAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.jdom2.Namespace;

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
        filterButton.setWidgetId(widgetId);
        N2oPerform filterAction = new N2oPerform();
        filterAction.setType(p.resolve(property("n2o.api.action.filters.type"), String.class));
        Map<N2oNamespace, Map<String, String>> props = new HashMap<>();
        props.put(new N2oNamespace(Namespace.NO_NAMESPACE), Collections.singletonMap("widgetId", widgetId));
        filterAction.setExtAttributes(props);
        filterButton.setAction(filterAction);
        filterButton.setModel(ReduxModel.filter);
        return filterButton;
    }

    public static N2oButton generateRefresh(CompileProcessor p) {
        N2oButton refreshButton = new N2oButton();
        refreshButton.setDescription(p.getMessage("n2o.api.action.toolbar.button.refresh.description"));
        refreshButton.setIcon("fa fa-refresh");
        N2oRefreshAction refreshAction = new N2oRefreshAction();
        refreshButton.setAction(refreshAction);
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

}
