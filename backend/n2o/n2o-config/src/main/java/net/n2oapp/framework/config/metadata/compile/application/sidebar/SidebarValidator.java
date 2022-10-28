package net.n2oapp.framework.config.metadata.compile.application.sidebar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор боковой панели приложения
 */
@Component
public class SidebarValidator extends TypedMetadataValidator<N2oSidebar> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSidebar.class;
    }

    @Override
    public void validate(N2oSidebar sidebar, SourceProcessor p) {
        if (sidebar.getMenu() != null)
            p.checkForExists(sidebar.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for sidebar");
        if (sidebar.getExtraMenu() != null)
            p.checkForExists(sidebar.getExtraMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for sidebar");

        if (sidebar.getDatasource() != null) {
            if (sidebar.getDatasourceId() != null)
                throw new N2oMetadataValidationException(
                        String.format("Сайдбар использует внутренний источник данных и ссылку на источник данных '%s' из скоупа application.xml одновременно",
                                sidebar.getDatasourceId()));
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            N2oStandardDatasource datasource = sidebar.getDatasource();
            if (dataSourcesScope.containsKey(datasource.getId()))
                throw new N2oMetadataValidationException(
                        String.format("Идентификатор '%s' внутреннего источника данных сайдбара уже используется источником данных из скоупа application.xml",
                                datasource.getId()));
        }
        if (sidebar.getDatasourceId() != null) {
            ValidationUtils.checkForExistsDatasource(sidebar.getDatasourceId(), p.getScope(DatasourceIdsScope.class),
                String.format("Сайдбар ссылается на несуществующий источник данных '%s'", sidebar.getDatasourceId()));
        }
        checkPath(sidebar, p.getScope(SidebarPathsScope.class));
    }

    private void checkPath(N2oSidebar sidebar, SidebarPathsScope scope) {
        if (scope == null)
            return;

        String sidebarPath = sidebar.getPath();
        if (scope.contains(sidebarPath)) {
            String errorMessage = sidebarPath == null
                    ? "More than one sidebar does not contain a path"
                    : String.format("The %s path is already taken by one of the sidebars", sidebarPath);
            throw new N2oMetadataValidationException(errorMessage);
        } else {
            scope.add(sidebarPath);
        }
    }
}
