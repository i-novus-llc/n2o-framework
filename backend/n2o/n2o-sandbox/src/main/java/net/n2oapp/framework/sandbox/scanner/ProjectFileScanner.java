package net.n2oapp.framework.sandbox.scanner;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.templates.TemplateModel;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

public class ProjectFileScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private String projectId;
    private SourceTypeRegister typeRegister;
    private FileStorage fileStorage;
    private ProjectTemplateHolder templatesHolder;

    public ProjectFileScanner(String projectId, SourceTypeRegister typeRegister, FileStorage fileStorage,
                              ProjectTemplateHolder templatesHolder) {
        this.projectId = projectId;
        this.typeRegister = typeRegister;
        this.fileStorage = fileStorage;
        this.templatesHolder = templatesHolder;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        List<FileModel> files = null;
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel == null) {
            files = fileStorage.getProjectFiles(projectId);
        } else {
            files = findResources(templateModel.getTemplateId());
        }
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
