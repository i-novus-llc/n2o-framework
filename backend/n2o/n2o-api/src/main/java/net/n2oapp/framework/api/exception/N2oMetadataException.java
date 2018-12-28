package net.n2oapp.framework.api.exception;

import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;

/**
 * Ошибка метаданных
 */
public class N2oMetadataException extends N2oException {
    private SourceMetadata sourceMetadata;
    private CompiledMetadata compiledMetadata;
    private ClientMetadata clientMetadata;

    public N2oMetadataException(CompiledMetadata compiledMetadata, String message) {
        super(message);
        this.compiledMetadata = compiledMetadata;
    }

    public N2oMetadataException(SourceMetadata sourceMetadata, String message) {
        super(message);
        this.sourceMetadata = sourceMetadata;
    }

    public N2oMetadataException(ClientMetadata clientMetadata, String message) {
        super(message);
        this.clientMetadata = clientMetadata;
    }

    public N2oMetadataException(SourceMetadata sourceMetadata, String message, Throwable cause) {
        super(message, cause);
        this.sourceMetadata = sourceMetadata;
    }

    public N2oMetadataException(CompiledMetadata compiledMetadata, String message, Throwable cause) {
        super(message, cause);
        this.compiledMetadata = compiledMetadata;
    }

    public N2oMetadataException(ClientMetadata clientMetadata, String message, Throwable cause) {
        super(message, cause);
        this.clientMetadata = clientMetadata;
    }

    public N2oMetadataException(SourceMetadata source) {
        this.sourceMetadata = source;
    }

    public N2oMetadataException(CompiledMetadata compiled) {
        this.compiledMetadata = compiled;
    }

    public N2oMetadataException(ClientMetadata client) {
        this.clientMetadata = client;
    }

    public SourceMetadata getSourceMetadata() {
        return sourceMetadata;
    }

    public CompiledMetadata getCompiledMetadata() {
        return compiledMetadata;
    }

    public ClientMetadata getClientMetadata() {
        return clientMetadata;
    }
}
