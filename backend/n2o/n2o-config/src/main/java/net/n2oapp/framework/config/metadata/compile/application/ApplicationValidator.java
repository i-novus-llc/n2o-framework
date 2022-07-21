package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarPathsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор приложения
 */
@Component
public class ApplicationValidator extends TypedMetadataValidator<N2oApplication> {

    @Override
    public Class<N2oApplication> getSourceClass() {
        return N2oApplication.class;
    }

    @Override
    public void validate(N2oApplication application, SourceProcessor p) {
        if (application.getHeader() != null)
            checkHeader(application.getHeader(), p);

        DataSourcesScope dataSourcesScope = new DataSourcesScope();
        p.safeStreamOf(application.getDatasources()).forEach(d -> dataSourcesScope.put(d.getId(), d));

        if (application.getSidebars() != null) {
            SidebarPathsScope sidebarsPaths = new SidebarPathsScope();
            for (N2oSidebar sidebar : application.getSidebars())
                p.validate(sidebar, sidebarsPaths, dataSourcesScope);
        }
    }

    private void checkHeader(N2oHeader header, SourceProcessor p) {
        if (header.getMenu() != null) {
            p.checkForExists(header.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header");
        }
        if (header.getExtraMenu() != null) {
            p.checkForExists(header.getExtraMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header");
        }
    }
}