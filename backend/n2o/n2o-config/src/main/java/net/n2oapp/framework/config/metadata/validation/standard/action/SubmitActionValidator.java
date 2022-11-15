package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор для действия <submit>
 */
@Component
public class SubmitActionValidator implements SourceValidator<N2oSubmitAction>, SourceClassAware {

    @Override
    public void validate(N2oSubmitAction source, SourceProcessor p) {
        DataSourcesScope datasources = p.getScope(DataSourcesScope.class);
        String datasourceId = getDatasourceId(source, p);
        if (datasourceId == null)
            throw new N2oMetadataValidationException("В действии <submit> не задан 'datasource'");
        checkDatasources(datasourceId, datasources);
    }

    private String getDatasourceId(N2oSubmitAction source, SourceProcessor p) {
        if (source.getDatasourceId() != null)
            return source.getDatasourceId();

        return ComponentScope.getFirstNotNull(p.getScope(ComponentScope.class),
                DatasourceIdAware.class, DatasourceIdAware::getDatasourceId);
    }

    private void checkDatasources(String datasourceId, DataSourcesScope datasources) {
        if (!datasources.containsKey(datasourceId))
            throw new N2oMetadataValidationException("Атрибут 'datasource' действия <submit> ссылается на несуществующий источник данных");

        N2oAbstractDatasource datasource = datasources.get(datasourceId);

        if (datasource instanceof Submittable) {
            if (((Submittable) datasource).getSubmit() == null)
                throw new N2oMetadataValidationException(String.format(
                        "Действие <submit> ссылается на источник данных '%s', в котором не определен submit", datasource.getId()));
        } else
            throw new N2oMetadataValidationException(String.format(
                    "Действие <submit> ссылается на источник данных '%s', который не поддерживает submit", datasource.getId()));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmitAction.class;
    }

}
