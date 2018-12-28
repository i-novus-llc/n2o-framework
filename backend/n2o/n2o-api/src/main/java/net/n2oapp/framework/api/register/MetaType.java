package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.util.Objects;

public class MetaType {
    private String sourceType;
    private Class<? extends SourceMetadata> baseSourceClass;

    public MetaType(String sourceType, Class<? extends SourceMetadata> baseSourceClass) {
        if (sourceType == null)
            throw new IllegalArgumentException("SourceType must not be null");
        if (baseSourceClass == null)
            throw new IllegalArgumentException("BaseSourceClass must not be null");
        this.sourceType = sourceType;
        this.baseSourceClass = baseSourceClass;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Class<? extends SourceMetadata> getBaseSourceClass() {
        return baseSourceClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaType)) return false;
        MetaType metaType = (MetaType) o;
        return Objects.equals(sourceType, metaType.sourceType) &&
                Objects.equals(baseSourceClass, metaType.baseSourceClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, baseSourceClass);
    }

    @Override
    public String toString() {
        return sourceType + ":" + baseSourceClass.getSimpleName();
    }
}
