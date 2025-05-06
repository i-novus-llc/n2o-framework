package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oSourceTypesPack;
import net.n2oapp.framework.config.register.OriginEnum;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.scanner.FolderInfoScanner;
import net.n2oapp.framework.config.register.storage.PathUtil;


/**
 * Аналог Info для тестов
 */
public class CompileInfo extends SourceInfo {

    private static SourceTypeRegister sourceTypes = new N2oApplicationBuilder()
            .packs(new N2oSourceTypesPack())
            .getEnvironment().getSourceTypeRegister();

    protected OriginEnum origin = OriginEnum.xml;
    private String path;
    private Class<? extends SourceMetadata> sourceMetadataClass;

    public CompileInfo(XmlInfo info) {
        origin = info.getOrigin();
        path = info.getLocalPath() != null && !info.getLocalPath().isEmpty() ? info.getLocalPath() : info.getId();
        sourceMetadataClass = info.getBaseSourceClass();
    }

    public CompileInfo(String path) {
        this.path = PathUtil.convertPathToClasspathUri(path);
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

    @Override
    public Class<? extends MetadataScanner> getScannerClass() {
        return FolderInfoScanner.class;
    }

    @Override
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

    public void setSourceMetadataClass(Class<? extends SourceMetadata> sourceMetadataClass) {
        this.sourceMetadataClass = sourceMetadataClass;
    }

    public void setOrigin(OriginEnum origin) {
        this.origin = origin;
    }

    public OriginEnum getOrigin() {
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
