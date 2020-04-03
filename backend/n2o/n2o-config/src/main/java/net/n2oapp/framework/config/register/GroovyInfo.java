package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.reader.GroovySourceReader;
import net.n2oapp.framework.config.register.scanner.GroovyInfoScanner;

public class GroovyInfo extends FileInfo {

    public GroovyInfo(String id, Class<? extends SourceMetadata> baseSourceClass, String uri, String localPath) {
        super(id, baseSourceClass, uri, localPath);
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return GroovySourceReader.class;
    }

    @Override
    public Class<? extends MetadataScanner> getScannerClass() {
        return GroovyInfoScanner.class;
    }
}
