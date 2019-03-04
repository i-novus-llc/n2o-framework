package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.config.register.ConfigId;

/**
 * Исключение, метаданная не найдена
 */
public class ReferentialIntegrityViolationException extends N2oException {

    private String id;
    private Class<? extends SourceMetadata> metadataClass;

    public ReferentialIntegrityViolationException(ConfigId configId) {
        super("[" + configId + "] not found");
        this.id = configId.getId();
        this.metadataClass = configId.getBaseSourceClass();
    }

    public ReferentialIntegrityViolationException(String id, Class<? extends SourceMetadata> metadataClass) {
        super("[" + id + "." + metadataClass.getSimpleName() + "] not found");
        this.id = id;
        this.metadataClass = metadataClass;
    }

    public String getId() {
        return id;
    }

    public Class<? extends SourceMetadata> getMetadataClass() {
        return metadataClass;
    }
}
