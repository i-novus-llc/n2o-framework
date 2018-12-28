package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;

public abstract class FileInfo extends SourceInfo {
    protected String uri;
    protected String localPath;

    public FileInfo() {
    }

    public FileInfo(String id, Class<? extends SourceMetadata> baseSourceClass, String uri, String localPath) {
        super(id, baseSourceClass);
        this.uri = uri;
        this.localPath = localPath;
    }

    public String getFileName() {
        return localPath != null ? localPath.substring(localPath.lastIndexOf('/') + 1, localPath.length()) : id + "." + baseSourceClass.getSimpleName();
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getUri() {
        return uri;
    }
}
