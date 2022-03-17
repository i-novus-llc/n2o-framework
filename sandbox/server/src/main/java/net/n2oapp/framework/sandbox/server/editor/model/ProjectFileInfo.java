package net.n2oapp.framework.sandbox.server.editor.model;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.sandbox.server.loader.ProjectFileLoader;

/**
 * Информация о метаданной из файлов проекта
 */
@Getter
@Setter
public class ProjectFileInfo extends SourceInfo {

    private String source;
    private Class<? extends MetadataScanner> scannerClass;

    public ProjectFileInfo(String id, Class<? extends SourceMetadata> baseSourceClass) {
        super(id, baseSourceClass);
    }

    @Override
    public Class<? extends SourceLoader> getReaderClass() {
        return ProjectFileLoader.class;
    }

}
