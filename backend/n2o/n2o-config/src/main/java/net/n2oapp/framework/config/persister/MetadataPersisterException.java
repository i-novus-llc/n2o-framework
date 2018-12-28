package net.n2oapp.framework.config.persister;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * @author rgalina
 * @since 21.02.2017
 */
public class MetadataPersisterException extends N2oException {
    public static void throwPersisterLock() {
        throw new MetadataPersisterException("Persist metadata is locked");
    }

    public MetadataPersisterException(String message) {
        super(message);
    }
}
