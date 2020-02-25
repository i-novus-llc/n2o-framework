package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;

public class JavaInfo extends SourceInfo {

    public JavaInfo(String id, Class<? extends SourceMetadata> baseSourceClass) {
        super(id, baseSourceClass);
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return JavaSourceLoader.class;
    }

    @Override
    public Class<? extends MetadataScanner> getScannerClass() {
        return JavaInfoScanner.class;
    }
}
