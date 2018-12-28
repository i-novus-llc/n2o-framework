package net.n2oapp.framework.api.metadata.reader;

/**
 * Сервис блокировки чтения и изменения метаданных (ConfigMetadataReader и MetadataPersister)
 * Они блокируются когда выполняются команды из аудита, такие как merge, updateSystem, pull
 */
public interface ConfigMetadataLocker {

    boolean isLocked();

    void lock();

    void unlock();
}
