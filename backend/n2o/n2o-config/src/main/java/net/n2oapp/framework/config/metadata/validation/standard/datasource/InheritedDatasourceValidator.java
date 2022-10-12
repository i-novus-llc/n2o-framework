package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
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
        checkSourceDatasource(source, ids, p);
        checkSubmitTargetDatasource(source, ids, p);
    }

    private void checkSourceDatasource(N2oInheritedDatasource source, DatasourceIdsScope ids, SourceProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        String msg = "Атрибут 'source-datasource' источника данных '%s' ссылается на несуществующий источник '%s'" +
                (componentScope != null && componentScope.unwrap(N2oAbstractPageAction.class) != null
                        && source.getSourcePage() != PageRef.THIS ?
                        " родительской страницы" : "");

        String formattedMessage = String.format(msg, source.getId(), source.getSourceDatasource());
        ValidationUtils.checkForExistsDatasource(source.getSourceDatasource(), ids, formattedMessage);
    }

    private void checkSubmitTargetDatasource(N2oInheritedDatasource source, DatasourceIdsScope ids, SourceProcessor p) {
        if (source.getSubmit() == null) return;

        ComponentScope componentScope = p.getScope(ComponentScope.class);
        String msg = "Атрибут 'target-datasource' элемента 'submit' источника данных '%s' ссылается на несуществующий источник '%s'" +
                (componentScope != null && componentScope.unwrap(N2oAbstractPageAction.class) != null
                        && source.getSourcePage() != PageRef.THIS ?
                        " родительской страницы" : "");

        String targetDatasource = source.getSubmit().getTargetDatasource();
        String formattedMessage = String.format(msg, source.getId(), targetDatasource);
        ValidationUtils.checkForExistsDatasource(targetDatasource, ids, formattedMessage);
    }
}
