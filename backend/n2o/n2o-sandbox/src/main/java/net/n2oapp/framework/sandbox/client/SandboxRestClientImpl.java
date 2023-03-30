package net.n2oapp.framework.sandbox.client;

import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Реализация клиента для отправки запросов на ApiController
 */
public class SandboxRestClientImpl implements SandboxRestClient {

    @Value("${n2o.sandbox.url}/project")
    private String baseApiProjectUrl;
    private RestTemplate restTemplate;

    public SandboxRestClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ProjectModel getProject(String projectId) {
        return restTemplate.getForObject(baseApiProjectUrl + normalize(projectId), ProjectModel.class);
    }

    @Override
    public String getFile(String projectId, String file) {
        return restTemplate.getForObject(baseApiProjectUrl + normalize(projectId) + normalize(file), String.class);
    }

    @Override
    public boolean isProjectExists(String projectId) {
        ResponseEntity<ProjectModel> result = restTemplate.exchange(baseApiProjectUrl + normalize(projectId), HttpMethod.HEAD, null, ProjectModel.class);
        return HttpStatus.OK.equals(result.getStatusCode());
    }

    @Override
    public void putFiles(String projectId, List<FileModel> files) {
        HttpEntity<List<FileModel>> requestUpdate = new HttpEntity<>(files);
        restTemplate.put(baseApiProjectUrl + normalize(projectId), requestUpdate);
    }
}
