package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка чтения метаданных
 */
public class N2oMetadataReaderException extends N2oException {

    private String metadataId;
    private String type;
    private String path;

    public N2oMetadataReaderException(Exception cause, String metadataId, String path, String type) {
        super(cause.getMessage(), cause);
        this.metadataId = metadataId;
        this.path = path;
        this.type = type;
    }


    public String getMetadataId() {
        return metadataId;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

}
