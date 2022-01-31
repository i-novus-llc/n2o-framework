package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
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
        if(application.getHeader() != null)
            checkHeader(application.getHeader(), p);
        if (application.getSidebar() != null) {
            checkSidebar(application.getSidebar(), p);
        }
    }

    private void checkSidebar(N2oSidebar sidebar, SourceProcessor p) {
        if (sidebar.getMenu() != null) {
            p.checkForExists(sidebar.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header");
        }
        if (sidebar.getExtraMenu() != null) {
            p.checkForExists(sidebar.getExtraMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header");
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