package net.n2oapp.framework.sandbox.scanner;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;

@Getter
@Setter
public class ProjectFileInfo extends SourceInfo {

    private String source;

    public ProjectFileInfo(String id, Class<? extends SourceMetadata> baseSourceClass) {
        super(id, baseSourceClass);
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return ProjectFileLoader.class;
    }

    @Override
    public Class<? extends MetadataScanner> getScannerClass() {
        return ProjectFileScanner.class;
    }
}
