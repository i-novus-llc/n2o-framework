package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oSimpleHeader;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

import static net.n2oapp.framework.config.metadata.validation.ValidationUtil.isExists;

/**
 * @author V. Alexeev.
 */
public class SimpleHeaderValidator extends TypedMetadataValidator<N2oSimpleHeader> {

    @Override
    public Class<N2oSimpleHeader> getMetadataClass() {
        return N2oSimpleHeader.class;
    }

    @Override
    public void check(N2oSimpleHeader simpleHeader) {
        if ((simpleHeader.getMenu() != null) &&
                (simpleHeader.getMenu().getRefId() != null) &&
                (!isExists(simpleHeader.getMenu().getRefId(), N2oSimpleMenu.class))) {
            throw new N2oMetadataValidationException("Ref-id doesn't exist");
        }
    }

}