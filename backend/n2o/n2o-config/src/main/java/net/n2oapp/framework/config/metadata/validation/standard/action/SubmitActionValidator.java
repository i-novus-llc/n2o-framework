package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oSubmitAction;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор SubmitAction
 */
@Component
public class SubmitActionValidator implements SourceValidator<N2oSubmitAction>, SourceClassAware {

    @Override
    public void validate(N2oSubmitAction source, SourceProcessor p) {
        DataSourcesScope datasources = p.getScope(DataSourcesScope.class);
        String datasourceId = getDatasourceId(source, p);
        if (datasourceId == null)
            throw new N2oMetadataValidationException("Submit action does not specify 'datasource'");
        checkDatasources(datasourceId, datasources);
    }

    private String getDatasourceId(N2oSubmitAction source, SourceProcessor p) {
        if (source.getDatasource() != null)
            return source.getDatasource();

        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            DatasourceIdAware idAware = componentScope.unwrap(DatasourceIdAware.class);
            if (idAware != null)
                return idAware.getDatasourceId();
        }
        return null;
    }

    private void checkDatasources(String datasourceId, DataSourcesScope datasources) {
        if (!datasources.containsKey(datasourceId))
            throw new N2oMetadataValidationException("The attribute 'datasource' refers to a non-existent datasource");

        N2oAbstractDatasource abstractDatasource = datasources.get(datasourceId);
        boolean isNotSupportSubmit = true;

        if (abstractDatasource instanceof N2oBrowserStorageDatasource) {
            if (((N2oBrowserStorageDatasource) abstractDatasource).getSubmit() == null)
                throw new N2oMetadataValidationException("Submit tag is not specified in 'datasource'");
            isNotSupportSubmit = false;
        }
        if (abstractDatasource instanceof N2oStandardDatasource) {
            if (((N2oStandardDatasource) abstractDatasource).getSubmit() == null)
                throw new N2oMetadataValidationException("Submit tag is not specified in 'datasource'");
            isNotSupportSubmit = false;
        }

        if (isNotSupportSubmit)
            throw new N2oMetadataValidationException("The datasource doesn't support submit action");
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmitAction.class;
    }

}
