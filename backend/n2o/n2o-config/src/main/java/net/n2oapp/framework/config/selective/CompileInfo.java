package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.register.*;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.metadata.pack.N2oSourceTypesPack;


/**
 * Аналог Info для тестов
 */
public class CompileInfo extends SourceInfo {

    private static SourceTypeRegister sourceTypes = new N2oApplicationBuilder()
            .packs(new N2oSourceTypesPack())
            .getEnvironment().getSourceTypeRegister();

    protected Origin origin = Origin.xml;
    private String path;
    private NamespaceReader<? extends N2oMetadata> metadataReader;
    private Class<? extends Compiled> compiledMetadataClass;
    private Class<? extends SourceMetadata> sourceMetadataClass;

    public CompileInfo(XmlInfo info) {
        origin = info.getOrigin();
        path = info.getLocalPath() != null && !info.getLocalPath().isEmpty() ? info.getLocalPath() : info.getId();
        sourceMetadataClass = info.getBaseSourceClass();
    }

    public CompileInfo(String path) {
        this.path = PathUtil.convertPathToClasspathUri(path);
    }

    public CompileInfo(String path, NamespaceReader<? extends N2oMetadata> metadataReader,
                       Class<? extends Compiled> compiledMetadataClass) {
        this(path);
        this.metadataReader = metadataReader;
        this.compiledMetadataClass = compiledMetadataClass;
    }

    public CompileInfo(String path, NamespaceIO<? extends N2oMetadata> metadataIO,
                       Class<? extends Compiled> compiledMetadataClass) {
        this(path);
        this.metadataReader = new ProxyNamespaceIO<>(metadataIO);
        this.compiledMetadataClass = compiledMetadataClass;
    }

    public CompileInfo(String id, Class<? extends SourceMetadata> metadataSourceClass) {
        this.id = id;
        this.sourceMetadataClass = metadataSourceClass;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Class<? extends SourceMetadata> getBaseSourceClass() {
        if (sourceMetadataClass != null)
            return sourceMetadataClass;
        else if (path != null) {
            String type = RegisterUtil.getIdAndPostfix(path)[1];
            if (type == null || type.isEmpty())
                return null;
            return sourceTypes.get(type).getBaseSourceClass();
        } else
            return null;
    }

    @Deprecated
    public Class<? extends SourceMetadata> getSourceMetadataClass() {
        return getBaseSourceClass();
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return SelectiveMetadataLoader.class;
    }

    public String getId() {
        if (id != null)
            return id;
        else if (path != null)
            return RegisterUtil.getIdAndPostfix(path)[0];
        else
            throw new IllegalStateException("Required id or path");
    }

    public String getPostfix() {
        return RegisterUtil.getIdAndPostfix(path)[1];
    }

    public void setCompiledMetadataClass(Class<? extends Compiled> compiledMetadataClass) {
        this.compiledMetadataClass = compiledMetadataClass;
    }

    public void setSourceMetadataClass(Class<? extends SourceMetadata> sourceMetadataClass) {
        this.sourceMetadataClass = sourceMetadataClass;
    }

    public void setMetadataReader(NamespaceReader<? extends N2oMetadata> metadataReader) {
        this.metadataReader = metadataReader;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return path;
    }

    public static void setSourceTypes(SourceTypeRegister sourceTypes) {
        CompileInfo.sourceTypes = sourceTypes;
    }
}
