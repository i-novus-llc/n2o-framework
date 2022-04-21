package net.n2oapp.framework.sandbox.scanner;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class ProjectFileScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private String projectId;
    private HttpSession session;
    private SourceTypeRegister typeRegister;
    private SandboxRestClient restClient;

    public ProjectFileScanner(String projectId, HttpSession session, SourceTypeRegister typeRegister, SandboxRestClient restClient) {
        this.projectId = projectId;
        this.session = session;
        this.typeRegister = typeRegister;
        this.restClient = restClient;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        ProjectModel projectModel = restClient.getProject(projectId, session);
        List<ProjectFileInfo> result = new ArrayList<>();
        if (projectModel != null) {
            for (FileModel fm : projectModel.getFiles()) {
                if (FileNameUtil.isSourceFile(fm.getFile())) {
                    Class<? extends SourceMetadata> baseSourceClass =
                            typeRegister.get(FileNameUtil.getTypeFromFile(fm.getFile())).getBaseSourceClass();
                    String name = FileNameUtil.getNameFromFile(fm.getFile());
                    ProjectFileInfo fileInfo = new ProjectFileInfo(name, baseSourceClass);
                    fileInfo.setSource(fm.getSource());
                    result.add(fileInfo);
                }
            }
        }

        return result;
    }
}
