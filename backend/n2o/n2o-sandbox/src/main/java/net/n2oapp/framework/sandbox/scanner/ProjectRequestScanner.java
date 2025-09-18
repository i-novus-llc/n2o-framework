package net.n2oapp.framework.sandbox.scanner;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Сканер для считывания проекта пришедшего в запросе
 */
public class ProjectRequestScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private SourceTypeRegister typeRegister;
    private List<FileModel> files;

    public ProjectRequestScanner(SourceTypeRegister typeRegister, List<FileModel> files) {
        this.typeRegister = typeRegister;
        this.files = files;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        List<ProjectFileInfo> result = new ArrayList<>();
        for (FileModel fm : files) {
            if (FileNameUtil.isSourceFile(fm.getFile())) {
                Class<? extends SourceMetadata> baseSourceClass =
                        typeRegister.get(FileNameUtil.getTypeFromFile(fm.getFile())).getBaseSourceClass();
                String name = FileNameUtil.getNameFromFile(fm.getFile());
                ProjectFileInfo fileInfo = new ProjectFileInfo(name, baseSourceClass);
                fileInfo.setSource(fm.getSource());
                result.add(fileInfo);
            }
        }
        return result;
    }
}
