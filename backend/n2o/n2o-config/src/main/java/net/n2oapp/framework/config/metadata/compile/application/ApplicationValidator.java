package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
        if (application.getSidebars() != null) {
            Set<String> sidebarsPaths = new HashSet<>();
            for (N2oSidebar sidebar : application.getSidebars())
                checkSidebar(sidebar, p, sidebarsPaths);
        }
    }

    private void checkSidebar(N2oSidebar sidebar, SourceProcessor p, Set<String> paths) {
        if (sidebar.getMenu() != null) {
            p.checkForExists(sidebar.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for sidebar");
        }
        if (sidebar.getExtraMenu() != null) {
            p.checkForExists(sidebar.getExtraMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for sidebar");
        }

        String sidebarPath = sidebar.getPath();
        if (paths.contains(sidebarPath)) {
            String errorMessage = sidebarPath == null
                    ? "More than one sidebar does not contain a path"
                    : String.format("The %s path is already taken by one of the sidebars", sidebarPath);
            throw new N2oMetadataValidationException(errorMessage);
        } else {
            paths.add(sidebarPath);
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