package net.n2oapp.framework.sandbox.client;

import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Реализация клиента для отправки запросов на ApiController
 */
public class SandboxRestClientImpl implements SandboxRestClient {

    @Value("${n2o.sandbox.api.url}")
    private String baseApiUrl;
    private RestTemplate restTemplate;

    public SandboxRestClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ProjectModel getProject(String projectId, HttpSession session) {
        return restTemplate.getForObject(baseApiUrl + normalize(projectId), ProjectModel.class);
    }

    @Override
    public String getFile(String projectId, String file, HttpSession session) {
        return restTemplate.getForObject(baseApiUrl + normalize(projectId) + normalize(file), String.class);
    }

    @Override
    public Boolean isProjectExists(String projectId) {
        return restTemplate.getForObject(baseApiUrl + normalize(projectId) + "/nonexistent", Boolean.class);
    }
}
