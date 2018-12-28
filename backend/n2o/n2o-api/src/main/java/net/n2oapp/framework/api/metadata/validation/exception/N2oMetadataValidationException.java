package net.n2oapp.framework.api.metadata.validation.exception;

import net.n2oapp.framework.api.exception.N2oMetadataException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

/**
 * Ошибка валидации метаданных
 */
public class N2oMetadataValidationException extends N2oMetadataException {

    public N2oMetadataValidationException(String message) {
        super((N2oMetadata) null, message);
    }

    public N2oMetadataValidationException(SourceMetadata metadata, String techMessage) {
        super(metadata, techMessage);
    }

    public N2oMetadataValidationException(SourceMetadata metadata, String techMessage, Exception e) {
        super(metadata, techMessage, e);
    }
}
