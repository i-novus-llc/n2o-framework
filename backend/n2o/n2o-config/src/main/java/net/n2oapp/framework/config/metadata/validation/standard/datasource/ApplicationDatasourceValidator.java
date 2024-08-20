package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.Objects.isNull;

/**
 * Валидатор источника данных, ссылающегося на источник из application.xml
 */
@Component
public class ApplicationDatasourceValidator extends AbstractDatasourceValidator<N2oApplicationDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oApplicationDatasource.class;
    }

    @Override
    public void validate(N2oApplicationDatasource datasource, SourceProcessor p) {
        super.validate(datasource, p);
        N2oApplication n2oApplication = p.getSource(p.resolve("${n2o.application.id}", String.class), N2oApplication.class);
        if (isNull(n2oApplication.getDatasources()) ||
                Arrays.stream(n2oApplication.getDatasources()).noneMatch(ds -> ds.getId()
                        .equals(getDatasourceId(datasource))))
            throw new N2oMetadataValidationException(String.format("Источник данных <app-datasource> ссылается на несуществующий в %s.application.xml источник данных '%s'",
                    n2oApplication.getId(), getDatasourceId(datasource)));
    }

    private String getDatasourceId(N2oApplicationDatasource datasource) {
        return datasource.getSourceDatasource() == null ? datasource.getId() : datasource.getSourceDatasource();
    }
}
