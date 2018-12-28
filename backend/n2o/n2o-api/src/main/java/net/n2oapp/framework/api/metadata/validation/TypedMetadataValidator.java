package net.n2oapp.framework.api.metadata.validation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

import java.util.Arrays;
import java.util.List;

/**
 * User: iryabov
 * Date: 26.02.14
 * Time: 15:17
 */
public abstract class TypedMetadataValidator<T extends SourceMetadata> implements SourceValidator<T>, SourceClassAware {

    public abstract Class<T> getMetadataClass();

    @Override
    public Class<? extends Source> getSourceClass() {
        return getMetadataClass();
    }

    public abstract void check(T metadata);

    @Override
    public void validate(SourceMetadata sourceMetadata) throws N2oMetadataValidationException {
        check((T) sourceMetadata);
    }
}
