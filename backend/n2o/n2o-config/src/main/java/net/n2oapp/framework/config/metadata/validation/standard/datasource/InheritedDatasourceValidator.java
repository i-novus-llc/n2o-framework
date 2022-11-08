package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Валидатор источника данных, получающего данные из другого источника данных
 */
@Component
public class InheritedDatasourceValidator extends AbstractDataSourceValidator<N2oInheritedDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInheritedDatasource.class;
    }

    @Override
    public void validate(N2oInheritedDatasource source, SourceProcessor p) {
        checkSourceDatasource(source);
    }

    private void checkSourceDatasource(N2oInheritedDatasource source) {
        String sourceDatasource = source.getSourceDatasource();
        if (sourceDatasource == null)
            throw new N2oMetadataValidationException(
                    String.format("В источнике данных '%s' не задан атрибут 'source-datasource'", source.getId())
            );
        if (sourceDatasource.equals(source.getId()))
            throw new N2oMetadataValidationException(
                    String.format("Атрибут 'source-datasource' источника данных '%s' совпадает с 'id'",
                            source.getId())
            );
    }
}
