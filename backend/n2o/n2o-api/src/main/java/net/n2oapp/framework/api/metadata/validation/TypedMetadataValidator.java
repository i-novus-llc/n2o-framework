package net.n2oapp.framework.api.metadata.validation;

import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;

/**
 * Типихированный валидатор
 */
public abstract class TypedMetadataValidator<T> implements SourceValidator<T>, SourceClassAware {

}
