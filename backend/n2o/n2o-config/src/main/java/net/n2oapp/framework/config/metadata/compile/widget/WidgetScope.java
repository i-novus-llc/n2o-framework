package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.DatasourceUtil;

/**
 * Информация о виджете при сборке внутренних метаданных
 */
@Getter
public class WidgetScope {
    private String widgetId;
    private String datasourceId;
    private String clientWidgetId;
    private String clientDatasourceId;
    private ReduxModelEnum model;
    private N2oStandardDatasource inLineDatasource;
    private MetaActions actions;


    public WidgetScope(String widgetId, String datasourceId, N2oStandardDatasource inLineDatasource) {
        this.widgetId = widgetId;
        this.datasourceId = datasourceId;
        this.inLineDatasource = inLineDatasource;
    }

    public WidgetScope(String widgetId, String datasourceId, N2oStandardDatasource inLineDatasource, MetaActions actions) {
        this.widgetId = widgetId;
        this.datasourceId = datasourceId;
        this.inLineDatasource = inLineDatasource;
        this.actions = actions;
    }

    public WidgetScope(String widgetId, String datasourceId, ReduxModelEnum model, CompileProcessor p) {
        this.widgetId = widgetId;
        this.datasourceId = datasourceId;
        this.model = model;
        PageScope pageScope = p.getScope(PageScope.class);
        this.clientWidgetId = pageScope != null ? DatasourceUtil.getClientWidgetId(widgetId, pageScope.getPageId()) : widgetId;
        this.clientDatasourceId = DatasourceUtil.getClientDatasourceId(datasourceId, p);
    }

    public WidgetScope(String widgetId, N2oStandardDatasource inLineDatasource, ReduxModelEnum model, CompileProcessor p, MetaActions actions) {
        this.widgetId = widgetId;
        this.inLineDatasource = inLineDatasource;
        this.model = model;
        PageScope pageScope = p.getScope(PageScope.class);
        this.clientWidgetId = pageScope != null ? DatasourceUtil.getClientWidgetId(widgetId, pageScope.getPageId()) : widgetId;
        this.clientDatasourceId = DatasourceUtil.getClientDatasourceId(datasourceId, p);
        this.actions = actions;
    }
}
