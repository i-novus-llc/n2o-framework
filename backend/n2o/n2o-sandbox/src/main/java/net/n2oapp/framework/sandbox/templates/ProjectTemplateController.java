package net.n2oapp.framework.sandbox.templates;

import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@RestController
public class ProjectTemplateController {
    @Autowired
    private ProjectTemplateHolder templatesHolder;
    @Autowired
    private ProjectSearcher projectSearcher;

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

    @CrossOrigin(origins = "*")
    @GetMapping("/project/search")
    public List<SearchProjectModel> searchProjectMatches(@RequestParam(name = "q") String text) throws Exception {
        return projectSearcher.search(text);
    }


}
