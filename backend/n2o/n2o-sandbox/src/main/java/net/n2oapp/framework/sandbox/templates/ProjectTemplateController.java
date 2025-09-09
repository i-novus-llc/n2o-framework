package net.n2oapp.framework.sandbox.templates;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.sandbox.file_storage.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static net.n2oapp.framework.sandbox.templates.LocalGitDiffService.collectChangedPackagesFromPackage;
import static net.n2oapp.framework.sandbox.templates.LocalGitDiffService.runGitCommand;
import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@Slf4j
@RestController
public class ProjectTemplateController {
    @Autowired
    private ProjectTemplateHolder templatesHolder;
    @Autowired
    private ProjectSearcher projectSearcher;
    @Autowired
    private GitLabDiffService gitLabDiffService;

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
                                                   @RequestParam(name = "newTag") String newTag) {
        String resourcesPath = "backend/n2o/n2o-sandbox/src/main/resources";
        try {
            File gitRoot = findGitRoot(new File("."));
            Set<String> changedPackages = new HashSet<>();
            Set<String> maybeDeletedPackages = new HashSet<>();
            boolean anyUpdated = false;
            boolean anyDeleted = false;
            List<String> allOldFiles;
            List<String> allNewFiles;
            if (gitRoot == null) {
                gitLabDiffService.collectChangedPackages(resourcesPath, oldTag, newTag, maybeDeletedPackages, changedPackages);
                allOldFiles = gitLabDiffService.listFilesAtTag(oldTag, resourcesPath);
                allNewFiles = gitLabDiffService.listFilesAtTag(newTag, resourcesPath);
            } else {
                collectChangedPackagesFromPackage(gitRoot, resourcesPath, oldTag, newTag, maybeDeletedPackages, changedPackages);
                allOldFiles = runGitCommand(gitRoot, "ls-tree", "-r", "--name-only", oldTag, "--", resourcesPath);
                allNewFiles = runGitCommand(gitRoot, "ls-tree", "-r", "--name-only", newTag, "--", resourcesPath);
            }

            for (String pkg : changedPackages) {
                if (allOldFiles.stream().anyMatch(f -> f.contains(pkg))) {
                    anyUpdated = true;
                    break;
                }
            }

            for (String pkg : maybeDeletedPackages) {
                if (allNewFiles.stream().noneMatch(f -> f.contains(pkg))) {
                    anyDeleted = true;
                    break;
                }
            }

            ProjectsDiffResponse response = new ProjectsDiffResponse();

            if (anyUpdated || anyDeleted) {
                // if some projects were updated, then return all projects for reload
                response.setReload(true);
                response.setProjects(getAllProjects());
            } else {
                response.setReload(false);
                Map<String, ProjectModel> projects = new HashMap<>();
                for (String pkg : changedPackages) {
                    ProjectModel project = new ProjectModel();
                    project.setId(pkg.replace('/', '_'));
                    project.setFiles(findResources(pkg));
                    projects.put(project.getId(), project);
                }
                response.setProjects(projects);
            }

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