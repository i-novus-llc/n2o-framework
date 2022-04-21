package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    private SandboxRestClient restClient;
    @Autowired
    private HttpSession session;


    @Override
    public Object get(String name) {
        String projectId = ThreadLocalProjectId.getProjectId();
        properties.computeIfAbsent(projectId, this::createProperties);
        return getProperty(name, projectId);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        String projectId = ThreadLocalProjectId.getProjectId();
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
        Properties props = new Properties();
        String userProperties = restClient.getFile(projectId, USER_PROPERTIES, session);
        if (userProperties != null) {
            try (InputStream inputStream = new ByteArrayInputStream(userProperties.getBytes())) {
                props.load(inputStream);
            } catch (IOException e) {
                throw new N2oException(e);
            }

        }
        return props;
    }
}
