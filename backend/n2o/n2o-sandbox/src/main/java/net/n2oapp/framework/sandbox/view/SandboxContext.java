package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Реализация контекста для использования в sandbox
 */
@Component
public class SandboxContext implements ContextEngine {

    public static final String USER_PROPERTIES = "/user.properties";
    private static final String SESSION_PROJECT_SEPARATOR = "/";
    private final Map<String, Properties> properties = new ConcurrentHashMap<>();

    @Value("${n2o.config.path}")
    private String basePath;
    @Autowired
    private HttpSession session;


    @Override
    public Object get(String name) {
        String projectId = ThreadLocalProjectId.getProjectId();
        if (isTemplateProject(projectId)) {
            String sessionProjectId = getSessionProjectId(projectId);
            properties.put(sessionProjectId, createProperties(projectId));
            return getProperty(name, sessionProjectId);
        }

        properties.computeIfAbsent(projectId, this::createProperties);
        return getProperty(name, projectId);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        String projectId = ThreadLocalProjectId.getProjectId();
        if (isTemplateProject(projectId)) {
            String sessionProjectId = getSessionProjectId(projectId);
            properties.put(sessionProjectId, createProperties(projectId));
            properties.get(sessionProjectId).putAll(dataSet);
            return;
        }

        properties.computeIfAbsent(projectId, this::createProperties);
        properties.get(projectId).putAll(dataSet);
    }

    @Override
    public Object get(String param, Map<String, Object> cache) {
        return get(param);
    }

    @Override
    public void set(Map<String, Object> data, Map<String, Object> cache) {
        set(data);
    }

    @Override
    public void refresh() {
        String projectId = ThreadLocalProjectId.getProjectId();
        if (isTemplateProject(projectId)) {
            String sessionProjectId = getSessionProjectId(projectId);
            properties.remove(sessionProjectId);
        }
        properties.remove(projectId);
    }

    public void deleteSessionProjectsProperties(HttpSession session) {
        for (Map.Entry<String, Properties> entry : properties.entrySet()) {
            if (entry.getKey().startsWith(session.getId() + SESSION_PROJECT_SEPARATOR))
                properties.remove(entry.getKey());
        }
    }

    private Object getProperty(String name, String projectId) {
        Object property = properties.get(projectId).get(name);
        if (property != null && property.getClass() == String.class) {
            String p = (String) property;
            if (p.startsWith("[") && p.endsWith("]")) {
                return Stream.of(p.substring(1, p.length() - 1).split(",")).map(String::trim).collect(Collectors.toList());
            }
        }
        return property;
    }

    private Properties createProperties(String projectId) {
//        Properties props = new Properties();
//        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
//        String filename = basePath + SESSION_PROJECT_SEPARATOR + projectId + USER_PROPERTIES;
//        ProjectModel project = ProjectUtil.getFromSession(session, projectId);
//        if (templateModel == null && new File(filename).isFile()) {
//            try (FileInputStream inputStream = new FileInputStream(filename)) {
//                props.load(inputStream);
//            } catch (IOException e) {
//                throw new N2oException(e);
//            }
//        } else if (project != null) {
//            FileModel propsFile = project.getFiles()
//                    .stream().filter(f -> FileNameUtil.isPropertyFile(f.getFile()))
//                    .findFirst().orElse(null);
//            if (propsFile != null) {
//                try (InputStream propertiesResource = new ByteArrayInputStream(propsFile.getSource().getBytes())) {
//                    props.load(propertiesResource);
//                } catch (IOException e) {
//                    throw new N2oException(e);
//                }
//            }
//        } else if (templateModel != null) {
//            try (InputStream propertiesResource = getClass().getClassLoader()
//                    .getResourceAsStream(templateModel.getTemplateId() + USER_PROPERTIES)) {
//                if (propertiesResource != null)
//                    props.load(propertiesResource);
//            } catch (IOException e) {
//                throw new N2oException(e);
//            }
//        }
//        return props;
        return new Properties();
    }

    private String getSessionProjectId(String projectId) {
        return session.getId() + SESSION_PROJECT_SEPARATOR + projectId;
    }

    private boolean isTemplateProject(String projectId) {
//        return templatesHolder.getTemplateModel(projectId) != null;
        return false;
    }
}
