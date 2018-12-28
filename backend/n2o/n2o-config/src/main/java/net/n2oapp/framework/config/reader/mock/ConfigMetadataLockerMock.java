package net.n2oapp.framework.config.reader.mock;

import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;

public class ConfigMetadataLockerMock implements ConfigMetadataLocker {
    private  boolean flag = false;

    @Override
    public boolean isLocked() {
        return flag;
    }

    @Override
    public void lock() {
        flag = true;
    }

    @Override
    public void unlock() {
        flag = false;
    }
}
