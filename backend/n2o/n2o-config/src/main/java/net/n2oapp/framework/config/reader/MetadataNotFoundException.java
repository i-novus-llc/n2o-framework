package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;

import static net.n2oapp.framework.config.util.MetadataUtil.XML_BY_METADATA;

/**
 * Исключение, метаданная не найдена
 */
public class MetadataNotFoundException extends N2oException {

    public MetadataNotFoundException(String id, Class<? extends SourceMetadata> metadataClass) {
        super(String.format("Не найден файл %s%s или не зарегистрирована соответствующая ему метаданная %s",
                id,
                XML_BY_METADATA.containsKey(metadataClass) ? "." + XML_BY_METADATA.get(metadataClass) : "",
                metadataClass.getSimpleName()));
    }
}