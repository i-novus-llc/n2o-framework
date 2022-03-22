package net.n2oapp.framework.sandbox.utils;


import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.register.storage.PathUtil.convertPathToClasspathUri;

/**
 * Утилитный класс для работы с проектами (в сессии)
 */
public class ProjectUtil {

    public static final String MODIFIED_TEMPLATE_PROJECTS = "modifiedTemplateProjects";
    private static final String baseViewUrl = "/sandbox/view";

    /**
     * Получение проекта из сессии
     * @param session   Сессия
     * @param projectId Идентификатор проекта
     * @return          Проект или null, в случает отсутствии проекта с указанным идентификатором в сессии
     */
    public static ProjectModel getFromSession(HttpSession session, String projectId) {
        if (session == null || session.getAttribute(MODIFIED_TEMPLATE_PROJECTS) == null)
            return null;
        return  (ProjectModel) ((Map) session.getAttribute(MODIFIED_TEMPLATE_PROJECTS)).get(projectId);
    }

    /**
     * Удаление проекта из сессии
     * @param session   Сессия
     * @param projectId Идентификатор проекта для удаления
     */
    public static void deleteFromSession(HttpSession session, String projectId) {
        Map<String, ProjectModel> modifiedProjects = (Map) session.getAttribute(MODIFIED_TEMPLATE_PROJECTS);
        modifiedProjects.remove(projectId);
        session.setAttribute(MODIFIED_TEMPLATE_PROJECTS, modifiedProjects);
    }

    /**
     * Сохранение проекта в сессии
     * @param projectId Идентификатор проекта
     * @param project   Проект
     * @param session   Сессия, в которую сохраняется проект
     */
    public static void putInSession(String projectId, ProjectModel project, HttpSession session) {
        Map<String, ProjectModel> modifiedProjects = new HashMap<>();
        if (session.getAttribute(MODIFIED_TEMPLATE_PROJECTS) != null)
            modifiedProjects = (Map) session.getAttribute(MODIFIED_TEMPLATE_PROJECTS);
        modifiedProjects.put(projectId, project);
        session.setAttribute(MODIFIED_TEMPLATE_PROJECTS, modifiedProjects);
    }

//    /**
//     * Инициализация readOnly проекта
//     * @param templateModel Шаблон для инициализации проекта
//     * @return              Проект без файлов
//     */
//    public static ProjectModel initTemplateSessionProject(TemplateModel templateModel) {
//        String projectId = templateModel.getProjectId();
//        ProjectModel project = new ProjectModel();
//        project.setId(projectId);
//        project.setViewUrl(baseViewUrl + "/" + projectId + "/");
//        project.setName(templateModel.getName());
//
//        return project;
//    }

    /**
     * Получение файлов по пути
     * @param path Путь до файлов
     * @return     Список файлов
     */
    public static List<FileModel> findResources(String path) {
        String uri = convertPathToClasspathUri(path);
        return findFilesByUri(uri);
    }

    /**
     * Получение файлов по uri
     * @param uri uri файлов
     * @return    Список файлов
     */
    public static List<FileModel> findFilesByUri(String uri) {
        List<FileModel> files = new ArrayList<>();
        String pattern = PathUtil.convertUrlToPattern(uri, "*", "*");
        List<Node> nodes = FileSystemUtil.getNodesByLocationPattern(pattern);
        for (Node node : nodes) {
            FileModel file = new FileModel().setFile(node.getLocalPath()).setSource(node.retrieveContent());
            if (node.getLocalPath().endsWith("index.page.xml"))
                files.add(0, file);//index page is first
            else
                files.add(file);
        }
        return files;
    }
}
