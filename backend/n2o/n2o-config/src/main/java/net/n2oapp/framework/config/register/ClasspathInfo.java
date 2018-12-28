package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;

public class ClasspathInfo extends XmlInfo {

    public ClasspathInfo(String id, Class<? extends SourceMetadata> baseSourceClass, String classpathPath) {
        super(id, baseSourceClass, classpathPath, "classpath:");
    }
}
