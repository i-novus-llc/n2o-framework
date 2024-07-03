package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkQueryExists;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор исходного источника данных
 */
@Component
public class StandardDatasourceValidator extends DatasourceValidator<N2oStandardDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public void validate(N2oStandardDatasource datasource, SourceProcessor p) {
        super.validate(datasource, p);
        ValidationUtils.checkForExistsObject(datasource.getObjectId(),
                String.format("Источник данных %s", getIdOrEmptyString(datasource.getId())), p);
        N2oQuery query = checkQueryExists(datasource.getQueryId(),
                String.format("Источник данных %s", getIdOrEmptyString(datasource.getId())), p);
        checkSubmit(datasource.getId(), datasource.getSubmit(), p);
        checkPrefilters(datasource.getId(), datasource.getFilters(), query, p);
    }
}
