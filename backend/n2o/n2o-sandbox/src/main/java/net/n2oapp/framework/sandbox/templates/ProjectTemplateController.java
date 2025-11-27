package net.n2oapp.framework.sandbox.templates;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.sandbox.file_storage.model.ProjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static net.n2oapp.framework.sandbox.templates.LocalGitDiffService.collectChangedPackagesFromPackage;
import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@Slf4j
@RestController
public class ProjectTemplateController {
    private Logger logger = LoggerFactory.getLogger(ProjectTemplateController.class);
    @Autowired
    private ProjectTemplateHolder templatesHolder;
    @Autowired
    private ProjectSearcher projectSearcher;
    @Autowired
    private GitLabDiffService gitLabDiffService;
    @Value("${sandbox.projects.resources.path:backend/n2o/n2o-sandbox/src/main/resources}")
    private String sandboxProjectsResourcesPath;

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
    @GetMapping("/projects-diff")
    public ProjectsDiffResponse getNewProjectFiles(@RequestParam(name = "oldTag") String oldTag,
                                                   @RequestParam(name = "newTag") String newTag,
                                                   @RequestParam(name = "onlyNew", required = false, defaultValue = "true") Boolean onlyNew) {
        try {
            File gitRoot = findGitRoot(new File("."));
            Set<String> changedPackages = new HashSet<>();
            Set<String> maybeDeletedPackages = new HashSet<>();
            if (gitRoot == null) {
                logger.info("Git root not found, using gitlab diff");
                gitLabDiffService.collectChangedPackages(sandboxProjectsResourcesPath, oldTag, newTag, maybeDeletedPackages, changedPackages);
            } else {
                collectChangedPackagesFromPackage(gitRoot, sandboxProjectsResourcesPath, oldTag, newTag, maybeDeletedPackages, changedPackages);
            }

            ProjectsDiffResponse response = new ProjectsDiffResponse();

            if (Boolean.TRUE.equals(onlyNew)) {
                Map<String, ProjectModel> projects = new HashMap<>();
                for (String pkg : changedPackages) {
                    ProjectModel project = new ProjectModel();
                    project.setId(pkg.replace('/', '_'));
                    project.setFiles(findResources(pkg));
                    projects.put(project.getId(), project);
                }
                response.setProjects(projects);
            } else {
                response.setProjects(getAllProjects());
            }
            logger.info("Send response with {} projects", response.getProjects().size());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute changed packages between tags '" + oldTag + "' and '" + newTag + "'", e);
        }
    }

    private Map<String, ProjectModel> getAllProjects() {
        Map<String, ProjectModel> projects = new HashMap<>();
        List<CategoryModel> templates = templatesHolder.getProjectTemplates();
        for (CategoryModel category : templates) {
            for (SectionModel section : category.getSections()) {
                for (TemplateModel template : section.getTemplates()) {
                    ProjectModel project = new ProjectModel();
                    project.setId(template.getProjectId());
                    project.setFiles(findResources(template.getTemplateId()));
                    project.setName(template.getName());
                    projects.put(template.getProjectId(), project);
                }
            }
        }
        return projects;
    }

    private static File findGitRoot(File start) {
        File current = start.getAbsoluteFile();
        int maxLevels = 10;
        while (current != null && maxLevels-- > 0) {
            File dotGit = new File(current, ".git");
            if (dotGit.exists()) return current;
            current = current.getParentFile();
        }

        return null;
    }
}