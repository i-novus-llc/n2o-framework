package net.n2oapp.framework.config.metadata.validation.standard.application;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarPathsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.metadata.validation.standard.ValidatorDatasourceIdsScope;
import org.springframework.stereotype.Component;

@Component
public class SidebarValidator implements SourceValidator<N2oSidebar>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSidebar.class;
    }

    @Override
    public void validate(N2oSidebar sidebar, SourceProcessor p) {
        if (sidebar.getMenu() != null)
            p.validate(sidebar.getMenu(), p.getScope(ValidatorDatasourceIdsScope.class));
        if (sidebar.getExtraMenu() != null)
            p.validate(sidebar.getExtraMenu(), p.getScope(ValidatorDatasourceIdsScope.class));

        ValidatorDataSourcesScope dataSourcesScope = p.getScope(ValidatorDataSourcesScope.class);

        if (sidebar.getDatasource() != null) {
            if (sidebar.getDatasourceId() != null)
                throw new N2oMetadataValidationException(
                        String.format("Сайдбар использует внутренний источник данных и ссылку на источник данных %s одновременно",
                                ValidationUtils.getIdOrEmptyString(sidebar.getDatasourceId())));
            N2oStandardDatasource datasource = sidebar.getDatasource();
            if (dataSourcesScope != null && dataSourcesScope.containsKey(datasource.getId()))
                throw new N2oMetadataValidationException(
                        String.format("Идентификатор %s внутреннего источника данных сайдбара уже используется другим источником данных",
                                ValidationUtils.getIdOrEmptyString(datasource.getId())));
        }
        if (sidebar.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(sidebar.getDatasourceId(), p,
                    String.format("Сайдбар ссылается на несуществующий источник данных %s", ValidationUtils.getIdOrEmptyString(sidebar.getDatasourceId())));

        checkPath(sidebar, p.getScope(SidebarPathsScope.class));
    }

    private void checkPath(N2oSidebar sidebar, SidebarPathsScope scope) {
        if (scope == null)
            return;

        String sidebarPath = sidebar.getPath();
        if (scope.contains(sidebarPath)) {
            String errorMessage = sidebarPath == null
                    ? "Приложение имеет более одного <sidebar> не содержащих 'path'"
                    : String.format("Два или более <sidebar> имеют одинаковый 'path = %s'", sidebarPath);
            throw new N2oMetadataValidationException(errorMessage);
        } else {
            scope.add(sidebarPath);
        }
    }
}
