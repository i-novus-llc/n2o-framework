package net.n2oapp.framework.sandbox.server.editor;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.sandbox.server.editor.model.*;
import net.n2oapp.framework.sandbox.server.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.server.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.server.utils.ProjectUtil;
import net.n2oapp.framework.sandbox.server.view.ProjectFileUpdateEvent;
import net.n2oapp.framework.sandbox.server.view.ProjectRouteRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.register.storage.PathUtil.convertPathToClasspathUri;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;
import static net.n2oapp.framework.sandbox.server.utils.ProjectUtil.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${n2o.config.path}")
    private String basePath;
    @Value("${server.servlet.context-path}/view")
    private String baseViewUrl;
    @Value("${n2o.sandbox.templates:}")
    private String templatesPath;
    @Value("${n2o.sandbox.defaultTemplate:examples/hello_world}")
    private String defaultTemplate;
    @Value("${n2o.version:unknown}")
    private String n2oVersion;

    @Autowired
    private ProjectIdGenerator generator;
    @Autowired
    private N2oEventBus eventBus;
    @Autowired
    private ProjectRouteRegister projectRouteRegister;
    @Autowired
    private ContextEngine sandboxContext;
    @Autowired
    private TemplatesHolder templatesHolder;
    @Autowired
    private ProjectSearcher projectSearcher;
    @Autowired
    private XsdSchemaParser schemaParser;
    @Autowired
    private SandboxTestDataProviderEngine dataProviderEngine;

    @CrossOrigin(origins = "*")
    @GetMapping("/version")
    public String getVersion() {
        return n2oVersion;
    }

    @GetMapping("/project")
    public List<CategoryModel> getProjectTemplates() {
        return templatesHolder.getProjectTemplates();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/{projectId}")
    public ProjectModel getProject(@PathVariable("projectId") String projectId, HttpSession session) {
        if (session.getAttribute(MODIFIED_TEMPLATE_PROJECTS) != null) {
            ProjectModel project = (ProjectModel) ((Map) session.getAttribute(MODIFIED_TEMPLATE_PROJECTS)).get(projectId);
            if (project != null)
                return project;
        }
        if (isProjectNotExists(projectId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + projectId + " not found");
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);

        ProjectModel project = new ProjectModel();
        project.setId(projectId);
        project.setViewUrl(baseViewUrl + "/" + projectId + "/");

        if (templateModel == null) {
            project.setFiles(findFiles(projectId));
        } else {
            project.setFiles(findResources(templateModel.getTemplateId()));
            project.setName(templateModel.getName());
        }

        return project;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/search")
    public List<SearchProjectModel> searchProjectMatches(@RequestParam(name = "q") String text) throws Exception {
        return projectSearcher.search(text);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/schemas")
    public ResponseEntity<Resource> loadSchema(@RequestParam(name = "name") String schemaNamespace) throws IOException {
        Resource schema = schemaParser.getSchema(schemaNamespace);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + schema.getFilename() + "\"")
                .body(schema);
    }

    @PostMapping("/project")
    public ProjectModel createProject(@RequestBody(required = false) TemplateModel body) throws IOException {
        ProjectModel project = new ProjectModel();
        String projectId = generator.generate(basePath);
        project.setId(projectId);
        String template = body != null ? body.getTemplateId() : defaultTemplate;
        String path = templatesPath + "/" + template;
        List<FileModel> files = findResources(path);
        for (FileModel file : files) {
            saveResource(projectId, file.getFile(), path);
        }
        project.setFiles(files);
        return project;
    }

    public ProjectModel cloneProject(String exProjectId, HttpSession session) {
        String projectId = generator.generate(basePath);
        ProjectModel sessionProject = ProjectUtil.getFromSession(session, exProjectId);
        if (sessionProject != null) {
            sessionProject.setId(projectId);
            sessionProject.getFiles().forEach(file -> saveFile(projectId, file.getFile(), file.getSource()));
            dataProviderEngine.deleteSessionDataSets(session);
            ProjectUtil.deleteFromSession(session, exProjectId);
            try {
                ThreadLocalProjectId.setProjectId(exProjectId);
                sandboxContext.refresh();
            } finally {
                ThreadLocalProjectId.clear();
            }
            return sessionProject;
        }

        if (isProjectNotExists(exProjectId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + exProjectId + " not found");

        ProjectModel project = new ProjectModel();
        project.setId(projectId);
        List<FileModel> files;
        TemplateModel templateModel = templatesHolder.getTemplateModel(exProjectId);
        if (templateModel != null)
            files = ProjectUtil.findResources(templateModel.getTemplateId());
        else
            files = findFiles(exProjectId);
        files.forEach(file -> saveFile(projectId, file.getFile(), file.getSource()));
        project.setFiles(files);

        return project;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/{projectId}/{file}")
    public String getFile(@PathVariable("projectId") String projectId,
                          @PathVariable("file") String file) {
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel == null) {
            return getFileContent(basePath + "/" + projectId + "/" + file);
        } else {
            return getResourceContent(templateModel.getTemplateId() + "/" + file);
        }
    }

    @GetMapping("/project/template/{fileName}")
    public String create(@PathVariable("fileName") String fileName) {
        return getTemplate(fileName);
    }

    @PutMapping("/project/{projectId}")
    public void putFile(@PathVariable("projectId") String projectId,
                        @RequestBody List<FileModel> files, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            sandboxContext.refresh();
        } finally {
            ThreadLocalProjectId.clear();
        }
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel != null) {
            ProjectModel project = initTemplateSessionProject(templateModel);
            List<FileModel> oldFiles = findOldFiles(projectId, session);

            project.setFiles(merge(oldFiles, files));
            ProjectUtil.putInSession(projectId, project, session);
            return;
        }
        files.forEach(file ->
                saveFile(projectId, file.getFile(), file.getSource()));
    }

    @DeleteMapping("/project/{projectId}")
    public void removeFile(@PathVariable("projectId") String projectId,
                           @RequestBody List<String> files, HttpSession session) {
        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        if (templateModel != null) {
            ProjectModel project = initTemplateSessionProject(templateModel);
            List<FileModel> oldFiles = findOldFiles(projectId, session);

            project.setFiles(deleteFileFromSession(oldFiles, files));
            ProjectUtil.putInSession(projectId, project, session);
            return;
        }

        files.forEach(file ->
                deleteFile(projectId, file));
    }


    public boolean isProjectNotExists(String projectId) {
        if (StringUtils.isEmpty(projectId)) return true;
        return templatesHolder.getTemplateModel(projectId) == null && !Paths.get(basePath, projectId).toFile().exists();
    }

    private void saveResource(String folder, String fileName, String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path + "/" + fileName);
        FileSystemUtil.saveContentToFile(resource.getInputStream(), new File(basePath + "/" + folder + "/" + resource.getFilename()));
    }

    private void saveFile(String folder, String fileName, String source) {
        String fileFolder = basePath + "/" + folder + "/" + fileName;
        FileSystemUtil.saveContentToFile(source, new File(fileFolder));
        eventBus.publish(new ProjectFileUpdateEvent(this, folder, fileFolder));
    }

    private void deleteFile(String projectId, String file) {
        String uri = convertRootPathToUrl(basePath + "/" + projectId + "/" + file);
        FileSystemUtil.removeContentByUri(uri);
    }

    private List<FileModel> findFiles(String projectId) {
        String uri = convertRootPathToUrl(basePath + "/" + projectId);
        return findFilesByUri(uri);
    }

    private String getFileContent(String path) {
        String uri = convertRootPathToUrl(path);
        return FileSystemUtil.getContentByUri(uri, true);
    }

    private String getResourceContent(String path) {
        String uri = convertPathToClasspathUri(path);
        return FileSystemUtil.getContentByUri(uri, true);
    }

    private String getFileType(String fileName) {
        String[] spl = fileName.toLowerCase().split("\\.");
        if (spl.length > 2 && "xml".equals(spl[spl.length - 1])) {
            return spl[spl.length - 2] + "." + spl[spl.length - 1];
        }
        return null;
    }

    private String getTemplate(String fileName) {
        String type = getFileType(fileName);
        if (type != null) {
            FileModel fileModel = findFilesByUri("/templates").stream()
                    .filter(f -> type.equals(f.getFile())).findFirst().orElse(null);
            if (fileModel != null)
                return fileModel.getSource();
        }
        return "";
    }

    private List<FileModel> findOldFiles(String projectId, HttpSession session) {
        if (session.getAttribute(MODIFIED_TEMPLATE_PROJECTS) != null) {
            Map<String, ProjectModel> modifiedProjects = (Map) session.getAttribute(MODIFIED_TEMPLATE_PROJECTS);
            if (modifiedProjects.get(projectId) != null)
                return modifiedProjects.get(projectId).getFiles();
        }

        return findResources(templatesHolder.getTemplateModel(projectId).getTemplateId());
    }

    private List<FileModel> deleteFileFromSession(List<FileModel> templateFiles, List<String> files) {
        for (String f : files) {
            removeIfContain(templateFiles, f);
        }

        return templateFiles;
    }

    private List<FileModel> merge(List<FileModel> templateFiles, List<FileModel> files) {
        for (FileModel f : files) {
            removeIfContain(templateFiles, f.getFile());
            templateFiles.add(f);
        }
        return templateFiles;
    }

    private void removeIfContain(List<FileModel> fileModels, String fileName) {
        fileModels.removeIf(fm -> fileName.equals(fm.getFile()));
    }
}
