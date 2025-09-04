package net.n2oapp.framework.sandbox.templates;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.framework.sandbox.file_storage.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.*;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@Slf4j
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
    @GetMapping("/projects-diff")
    public ProjectsDiffResponse getNewProjectFiles(@RequestParam(name = "oldTag") String oldTag,
                                                   @RequestParam(name = "newTag") String newTag) {
        String resourcesPath = "backend/n2o/n2o-sandbox/src/main/resources";
        try {
            File gitRoot = findGitRoot(new File("."));
            Set<String> changedPackages = new HashSet<>();
            Set<String> maybeDeletedPackages = new HashSet<>();
            collectChangedPackages(gitRoot, resourcesPath, oldTag, newTag, maybeDeletedPackages, changedPackages);

            List<String> allOldFiles = runGitCommand(gitRoot, "ls-tree", "-r", "--name-only", oldTag, "--", resourcesPath);
            boolean anyUpdated = false;
            for (String pkg : changedPackages) {
                if (allOldFiles.stream().anyMatch(f -> f.contains(pkg))) {
                    anyUpdated = true;
                    break;
                }
            }

            List<String> allNewFiles = runGitCommand(gitRoot, "ls-tree", "-r", "--name-only", newTag, "--", resourcesPath);
            boolean anyDeleted = false;
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

    private static void collectChangedPackages(File gitRoot, String resourcesPath, String oldTag, String newTag,
                                               Set<String> maybeDeletedPackages, Set<String> changedPackages) throws IOException, InterruptedException {
        List<String> diffOutput = runGitCommand(gitRoot, "diff", "--name-status", oldTag, newTag, "--", resourcesPath);
        for (String line : diffOutput) {
            String path = null;
            String changes = "";
            if (line != null && !line.isBlank()) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    changes = parts[0];
                    String candidatePath = parts[parts.length - 1];
                    if (candidatePath.startsWith(resourcesPath)) {
                        path = candidatePath;
                    }
                }
            }
            if (path == null) {
                continue;
            }

            String pkg = extractDirectory(resourcesPath, path);
            if (pkg != null && !pkg.isBlank()) {
                if (changes.equals("D")) {
                    maybeDeletedPackages.add(pkg);
                } else {
                    changedPackages.add(pkg);
                }
            }
        }

        changedPackages.remove("menu");
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

        throw new IllegalStateException("Git repository root not found");
    }

    private static List<String> runGitCommand(File workingDir, String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }
        int code = p.waitFor();
        if (code != 0) {
            throw new IOException("Git command failed: " + String.join(" ", command) + "\nOutput: " + String.join("\n", output));
        }
        return output;
    }

    private static String extractDirectory(String basePath, String fullPath) {
        String normalized = fullPath.replace('\\', '/');
        String base = basePath.replace('\\', '/');
        if (!normalized.startsWith(base)) {
            return null;
        }
        String remainder = normalized.substring(base.length());
        if (remainder.startsWith("/")) {
            remainder = remainder.substring(1);
        }
        int lastSlash = remainder.lastIndexOf('/');
        if (lastSlash <= 0) {
            return null;
        }
        return remainder.substring(0, lastSlash);
    }
}