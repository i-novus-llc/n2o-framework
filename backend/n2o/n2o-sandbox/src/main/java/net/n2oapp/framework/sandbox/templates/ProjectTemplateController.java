package net.n2oapp.framework.sandbox.templates;

import net.n2oapp.framework.sandbox.file_storage.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    public List<SearchProjectModel> searchProjectMatches(@RequestParam(name = "q") String text) throws URISyntaxException, IOException {
        return projectSearcher.search(text);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/projects")
    public List<ProjectModel> getProjectFiles() {
        List<ProjectModel> projects = new ArrayList<>();
        List<CategoryModel> templates = templatesHolder.getProjectTemplates();
        for (CategoryModel category : templates) {
            for (SectionModel section : category.getSections()) {
                for (TemplateModel template : section.getTemplates()) {
                    ProjectModel project = new ProjectModel();
                    project.setId(template.getProjectId());
                    project.setFiles(findResources(template.getTemplateId()));
                    project.setName(template.getName());
                    projects.add(project);
                }
            }
        }
        return projects;
    }
}