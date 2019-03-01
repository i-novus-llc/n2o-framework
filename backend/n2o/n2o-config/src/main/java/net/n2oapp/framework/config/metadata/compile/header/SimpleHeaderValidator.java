package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oSimpleHeader;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import org.springframework.stereotype.Component;

/**
 * Валидатор хедера
 */
@Component
public class SimpleHeaderValidator extends TypedMetadataValidator<N2oSimpleHeader> {

    @Override
    public Class<N2oSimpleHeader> getSourceClass() {
        return N2oSimpleHeader.class;
    }

    @Override
    public void validate(N2oSimpleHeader simpleHeader, ValidateProcessor p) {
        if (simpleHeader.getMenu() != null) {
           p.checkForExists(simpleHeader.getMenu().getRefId(), N2oSimpleMenu.class, "Menu {0} doesn't exists for header " + simpleHeader.getId());
        }
    }

}