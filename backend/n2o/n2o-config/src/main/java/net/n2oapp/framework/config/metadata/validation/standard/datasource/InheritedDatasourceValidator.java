package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
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
        DatasourceIdsScope ids = p.getScope(DatasourceIdsScope.class);
        checkSourceDatasource(source, ids);
        checkSubmitTargetDatasource(source, ids);
    }

    private void checkSourceDatasource(N2oInheritedDatasource source, DatasourceIdsScope ids) {
        String msg = String.format("Атрибут 'source-datasource' источника данных '%s' ссылается на несуществующий источник '%s'",
                source.getId(), source.getSourceDatasource());
        ValidationUtils.checkForExistsDatasource(source.getSourceDatasource(), ids, msg);
    }

    private void checkSubmitTargetDatasource(N2oInheritedDatasource source, DatasourceIdsScope ids) {
        if (source.getSubmit() != null) {
            String targetDatasource = source.getSubmit().getTargetDatasource();
            String msg = String.format("Атрибут 'target-datasource' элемента 'submit' источника данных '%s' ссылается на несуществующий источник '%s'",
                    source.getId(), targetDatasource);
            ValidationUtils.checkForExistsDatasource(targetDatasource, ids, msg);
        }
    }
}
