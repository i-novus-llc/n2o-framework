package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.Objects.isNull;

/**
 * Валидатор источника данных, ссылающегося на источник из application.xml
 */
@Component
public class ApplicationDatasourceValidator extends AbstractDataSourceValidator<N2oApplicationDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oApplicationDatasource.class;
    }

    @Override
    public void validate(N2oApplicationDatasource datasource, SourceProcessor p) {
        setDatasourceId(datasource, p);
        N2oApplication n2oApplication = p.getSource(p.resolve("${n2o.application.id}", String.class), N2oApplication.class);
        if (isNull(n2oApplication.getDatasources()) || Arrays.stream(n2oApplication.getDatasources()).noneMatch(ds -> ds.getId().equals(datasource.getId())))
            throw new N2oMetadataValidationException(String.format("Источник данных '%s' ссылается на несуществующий в %s.application.xml источник данных '%s'",
                    datasourceId, n2oApplication.getId(), datasource.getId()));
    }
}
