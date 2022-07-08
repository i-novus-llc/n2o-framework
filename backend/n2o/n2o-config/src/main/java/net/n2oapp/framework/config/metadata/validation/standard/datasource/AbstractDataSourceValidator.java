package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

public abstract class AbstractDataSourceValidator<T extends N2oAbstractDatasource> implements SourceValidator<T>, SourceClassAware {

    protected String datasourceId;

    /**
     * Определение идентификатора источника данных для сообщений в исключениях
     *
     * @param datasource Источник данных
     * @param p          Процессор исходных метаданных
     */
    protected void setDatasourceId(T datasource, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            datasourceId = ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId());
        else
            datasourceId = datasource.getId();
    }
}
