package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор модели страницы с единственным виджетом
 */
@Component
public class SimplePageValidator implements SourceValidator<N2oSimplePage>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimplePage.class;
    }

    @Override
    public void validate(N2oSimplePage source, SourceProcessor p) {
        if (source.getWidget() == null)
            throw new N2oMetadataValidationException("Не задан виджет простой страницы");
        checkDatasource(source, p);
    }

    private void checkDatasource(N2oSimplePage source, SourceProcessor p) {
        DatasourceIdsScope datasourceIdsScope = new DatasourceIdsScope();
        if (source.getWidget() != null) {
            datasourceIdsScope.add(source.getWidget().getId());
            p.validate(source.getWidget(), datasourceIdsScope);
        }
    }
}
