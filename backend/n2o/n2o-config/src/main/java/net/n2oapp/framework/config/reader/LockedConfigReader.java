package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.config.register.XmlInfo;

import java.util.function.Supplier;

public class LockedConfigReader implements ConfigReader {
    private ConfigMetadataLocker locker;
    private ConfigReader delegate;

    public LockedConfigReader(ConfigReader delegate, ConfigMetadataLocker locker) {
        this.delegate = delegate;
        this.locker = locker;
    }

    @Override
    public <S extends SourceMetadata> S load(XmlInfo info, String params) {
        return checkLock(() -> delegate.load(info, params));
    }

    @Override
    public <T extends SourceMetadata> T read(String id, Class<T> metadataClass, CompileContext context) {
        return checkLock(() -> delegate.read(id, metadataClass, context));
    }

    @Override
    public <T extends SourceMetadata> T read(String id, String src, Class<T> metadataClass) {
        return checkLock(() -> delegate.read(id, src, metadataClass));
    }

    private <T> T checkLock(Supplier<T> task) {
        if (locker.isLocked())
            throw new MetadataReaderException("Reading metadata is locked");
        return task.get();
    }
}
