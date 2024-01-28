package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static net.n2oapp.framework.config.metadata.compile.ComponentScope.getFirstNotNull;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getSpaceWithIdOrEmptyString;

/**
 * Валидатор для действия <submit>
 */
@Component
public class SubmitActionValidator implements SourceValidator<N2oSubmitAction>, SourceClassAware {

    @Override
    public void validate(N2oSubmitAction source, SourceProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        String datasourceId = getDatasourceId(source, componentScope);

        if (datasourceId != null) {
            checkDatasourceById(datasourceId, p);
        } else {
            N2oStandardDatasource datasource = getFirstNotNull(componentScope, N2oWidget.class, N2oWidget::getDatasource);
            if (datasource != null)
                checkDatasourceByInstance(datasource);
            else
                throw new N2oMetadataValidationException("Для действия <submit> не задан 'datasource'");
        }
    }

    private String getDatasourceId(N2oSubmitAction source, ComponentScope componentScope) {
        if (source.getDatasourceId() != null)
            return source.getDatasourceId();

        return getFirstNotNull(componentScope, DatasourceIdAware.class, DatasourceIdAware::getDatasourceId);
    }

    private void checkDatasourceById(String datasourceId, SourceProcessor p) {
        ValidationUtils.checkDatasourceExistence(datasourceId, p,
                "Атрибут 'datasource' действия <submit> ссылается на несуществующий источник данных");
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (dataSourcesScope != null)
            checkDatasourceByInstance(dataSourcesScope.get(datasourceId));
    }

    private void checkDatasourceByInstance(@Nonnull N2oAbstractDatasource datasource) {
        if (datasource instanceof N2oParentDatasource || datasource instanceof N2oApplicationDatasource)
            return;
        if (datasource instanceof Submittable) {
            if (((Submittable) datasource).getSubmit() == null)
                throw new N2oMetadataValidationException(String.format(
                        "Действие <submit> использует источник данных%s, в котором не определен submit",
                        getSpaceWithIdOrEmptyString(datasource.getId())));
        } else
            throw new N2oMetadataValidationException(String.format(
                    "Действие <submit> использует источник данных%s, который не поддерживает submit",
                    getSpaceWithIdOrEmptyString(datasource.getId())));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmitAction.class;
    }

}
