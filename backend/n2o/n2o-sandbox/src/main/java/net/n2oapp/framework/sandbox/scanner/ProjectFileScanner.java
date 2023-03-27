package net.n2oapp.framework.sandbox.scanner;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.templates.TemplateModel;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

public class ProjectFileScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private String projectId;
    private SourceTypeRegister typeRegister;
    private SandboxRestClient restClient;
    private ProjectTemplateHolder templatesHolder;

    public ProjectFileScanner(String projectId, SourceTypeRegister typeRegister, SandboxRestClient restClient, ProjectTemplateHolder templatesHolder) {
        this.projectId = projectId;
        this.typeRegister = typeRegister;
        this.restClient = restClient;
        this.templatesHolder = templatesHolder;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        List<FileModel> files = null;
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel == null) {
            ProjectModel projectModel = restClient.getProject(projectId);
            files = projectModel == null ? new ArrayList<>() : projectModel.getFiles();
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
