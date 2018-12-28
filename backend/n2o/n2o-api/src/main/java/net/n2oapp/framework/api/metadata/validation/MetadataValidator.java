package net.n2oapp.framework.api.metadata.validation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

import java.util.List;

/**
 * User: operhod
 * Date: 14.01.14
 * Time: 11:29
 */
@Deprecated
public interface MetadataValidator extends SourceValidator<SourceMetadata>, SourceClassAware {

    List<Class> getGlobalMetadataClasses();

    @Override
    void validate(SourceMetadata sourceMetadata) throws N2oMetadataValidationException;

    @Override
    default Class<? extends Source> getSourceClass() {
        return getGlobalMetadataClasses().get(0);
    }
}
