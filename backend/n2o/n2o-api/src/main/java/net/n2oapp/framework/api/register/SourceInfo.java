package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.scan.MetadataScanner;

import java.io.Serializable;
import java.util.Objects;

/**
 * Информация о расположении метаданных
 */
public abstract class SourceInfo implements Serializable {
    protected String id;
    protected Class<? extends SourceMetadata> baseSourceClass;

    protected SourceInfo() {
    }

    public SourceInfo(String id,
                      Class<? extends SourceMetadata> baseSourceClass) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null");
        if (baseSourceClass == null)
            throw new IllegalArgumentException("BaseSourceClass must not be null");
        this.id = id;
        this.baseSourceClass = baseSourceClass;
    }

    public abstract Class<? extends SourceLoader> getReaderClass();

    public abstract Class<? extends MetadataScanner> getScannerClass();

    public String getId() {
        return id;
    }

    public Class<? extends SourceMetadata> getBaseSourceClass() {
        return baseSourceClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceInfo that)) return false;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getBaseSourceClass(), that.getBaseSourceClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBaseSourceClass());
    }

    @Override
    public String toString() {
        return getId() + "." + getBaseSourceClass().getSimpleName();
    }
}
