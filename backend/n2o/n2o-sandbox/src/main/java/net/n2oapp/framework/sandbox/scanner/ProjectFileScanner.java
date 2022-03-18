package net.n2oapp.framework.sandbox.scanner;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;
import net.n2oapp.framework.sandbox.scanner.model.FileModel;
import net.n2oapp.framework.sandbox.scanner.model.ProjectModel;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class ProjectFileScanner implements OverrideInfoScanner<ProjectFileInfo> {

    private String projectUrl;
    private HttpSession session;
    private SourceTypeRegister typeRegister;

    public ProjectFileScanner(String projectUrl, HttpSession session, SourceTypeRegister typeRegister) {
        this.projectUrl = projectUrl;
        this.session = session;
        this.typeRegister = typeRegister;
    }

    @Override
    public List<ProjectFileInfo> scan() {
        RestTemplate restTemplate = new RestTemplate();
        ProjectModel projectModel = restTemplate.postForObject(projectUrl, null, ProjectModel.class);
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
