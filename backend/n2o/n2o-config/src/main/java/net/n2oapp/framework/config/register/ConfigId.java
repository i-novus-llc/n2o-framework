package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetaType;

import java.io.Serializable;

/**
 * Полный идентификатор метаданной
 */
@Deprecated //use MetaKey
public class ConfigId implements Serializable {
    private String id;
    private String type;
    private Class<? extends SourceMetadata> baseSourceMetadataClass;

    public ConfigId(String id, MetaType metaType) {
        this.id = id;
        this.baseSourceMetadataClass = metaType.getBaseSourceClass();
        this.type = metaType.getSourceType();
    }

    public Class<? extends SourceMetadata> getBaseSourceClass() {
        return baseSourceMetadataClass;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigId configId = (ConfigId) o;

        if (!id.equals(configId.id)) return false;
        if (!type.equals(configId.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return id + '.' + type;

    }
}
