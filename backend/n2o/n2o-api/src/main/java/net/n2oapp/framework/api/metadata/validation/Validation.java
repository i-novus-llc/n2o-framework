package net.n2oapp.framework.api.metadata.validation;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

/**
 * User: operhod
 * Date: 14.01.14
 * Time: 11:51
 */
@Deprecated
public interface Validation extends SourceValidator<SourceMetadata> {


    void validate(SourceMetadata sourceMetadata) throws N2oMetadataValidationException;

}
