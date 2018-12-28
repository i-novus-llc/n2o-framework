package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.properties.StaticProperties;

import java.io.File;
import java.io.IOException;

/**
 * Сервис блокировки чтения и изменения метаданных (LockedXmlMetadataReader и MetadataPersister)
 * Они блокируются когда выполняются команды из аудита, такие как merge, updateSystem, pull
 */
public class ConfigMetadataLockerImpl implements ConfigMetadataLocker {
    private String lockFileName;

    public ConfigMetadataLockerImpl() {
        lockFileName = PathUtil.concatAbsoluteAndLocalPath(StaticProperties.getProperty("n2o.config.path"), "lock.tmp");
    }

    public ConfigMetadataLockerImpl(String configPath) {
        this.lockFileName = PathUtil.concatAbsoluteAndLocalPath(configPath, "lock.tmp");
    }

    /**
     * Проверяет заблокированы ли сейчас действия с метаданными
     * @return true если действия с метаданными запрещены, false иначе
     */
    public boolean isLocked() {
        File lockFile = new File(lockFileName);
        return lockFile.exists();
    }

    /**
     * Заблокировать доступ к метаданным
     */
    public void lock() {
        if (isLocked()) {
            throw new N2oException("Error in locking config metadata! One instance try to lock, when it has locked already by " + lockFileName + "!");
        }
        try {
            File lockFile = new File(lockFileName);
            lockFile.createNewFile();
        } catch (IOException e) {
            throw new N2oException("Error with lock config metadata! lockFileName = " + lockFileName, e);
        }
    }

    /**
     * Разблокировать доступ к метаданным
     */
    public void unlock() {
        File lockFile = new File(lockFileName);
        if (lockFile.exists()) {
            lockFile.delete();
        }
    }
}
