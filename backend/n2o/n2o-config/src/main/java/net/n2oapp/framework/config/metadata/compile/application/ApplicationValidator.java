package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
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
    public void validate(N2oApplication application, ValidateProcessor p) {
        //todo fix header for application
        /*if (simpleHeader.getMenu() != null) {
           p.checkForExists(simpleHeader.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header");
        }*/
    }

}