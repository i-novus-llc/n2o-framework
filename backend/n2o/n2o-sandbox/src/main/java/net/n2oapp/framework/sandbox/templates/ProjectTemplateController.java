package net.n2oapp.framework.sandbox.templates;

import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
public class ProjectTemplateController {
    @Autowired
    private ProjectTemplateHolder templatesHolder;

    @CrossOrigin(origins = "*")
    @GetMapping("/project")
    public List<CategoryModel> getProjectTemplates() {
        return templatesHolder.getProjectTemplates();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/{projectId}")
    public ProjectModel getProject(@PathVariable("projectId") String projectId) {
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel == null) {
            return null;
        } else {
            ProjectModel project = new ProjectModel();
            project.setId(projectId);
            project.setFiles(findResources(templateModel.getTemplateId()));
            project.setName(templateModel.getName());
            return project;
        }
    }


}
