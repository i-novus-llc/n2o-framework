package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.storage.PathUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Информация о метаданной
 */
public class XmlInfo extends FileInfo {
    protected boolean override;

    private Class<? extends MetadataScanner> scannerClass;

    @Deprecated protected ConfigId configId;
    protected Set<ConfigId> dependents = new HashSet<>();//ссылки на контекстныальные метаданные, которые нужно сбросить, при изменении этого файла
    @Deprecated protected Origin origin = Origin.xml;
    protected XmlInfo ancestor;

    @Deprecated
    protected XmlInfo() {

    }

    public XmlInfo(String id, Class<? extends SourceMetadata> baseSourceClass, String localPath, String configPath) {
        this.id = id;
        this.baseSourceClass = baseSourceClass;
        this.localPath = localPath;
        this.uri = PathUtil.concatAbsoluteAndLocalPath(configPath, localPath);
    }

    public XmlInfo(String id, Class<? extends SourceMetadata> baseSourceClass, String localPath) {
        this(id, baseSourceClass, localPath, "");
    }

    @Deprecated
    public XmlInfo(InfoConstructor constructor) {
        this.id = constructor.getId();
        this.baseSourceClass = constructor.getBaseSourceClass();
        this.uri = constructor.getUri();
        this.localPath = constructor.getLocalPath();
        configId = constructor.getConfigId();
        dependents = Collections.unmodifiableSet(constructor.getDependents());
        origin = constructor.getOrigin();
        ancestor = constructor.getAncestor();
    }

    @Deprecated
    public XmlInfo(ConfigId configId) {
        this.id = configId.getId();
        this.baseSourceClass = configId.getBaseSourceClass();
        this.configId = configId;
    }

    public String getType() {
        return configId.getType();
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static Class getClass(String cacheKey) {
        try {
            return Class.forName(cacheKey.split("\\$")[0]);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getId(String cacheKey) {
        return cacheKey.split("\\$")[1];
    }

    public XmlInfo getAncestor() {
        return ancestor;
    }

    public String getURI() {
        return this.uri;
    }

    public Origin getOrigin() {
        return origin;
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return XmlMetadataLoader.class;
    }

    @Override
    public Class<? extends MetadataScanner> getScannerClass() {
        return scannerClass;
    }

    public void setScannerClass(Class<? extends MetadataScanner> scannerClass) {
        this.scannerClass = scannerClass;
    }

    public ConfigId getConfigId() {
        return configId;
    }

    public Set<ConfigId> getDependents() {
        return dependents;
    }

    public String getDirectory() {
        int idx = getLocalPath().indexOf(getFileName());
        if (idx > 0)
            return getLocalPath().substring(0, idx - 1);
        return "";
    }

    public boolean isOverride() {
        return override;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlInfo)) return false;
        XmlInfo info = (XmlInfo) o;
        return Objects.equals(id, info.id) &&
                Objects.equals(baseSourceClass, info.baseSourceClass) &&
                Objects.equals(localPath, info.localPath) &&
                Objects.equals(uri, info.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseSourceClass, localPath, uri);
    }

    @Override
    public String toString() {
        return getFileName();
    }
}
