package net.n2oapp.framework.sandbox.client;

import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Реализация клиента для отправки запросов на ApiController
 */
public class SandboxRestClientImpl implements SandboxRestClient {

    private static final String PROJECT_PREFIX = "/project";
    private static final String TEMPLATES_PREFIX = "/templates";
    private static final String SCHEMAS_PREFIX = "/schemas";
    @Value("${n2o.sandbox.api.url}/project")
    private String baseApiProjectUrl;
    private RestTemplate restTemplate;

    public SandboxRestClientImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ProjectModel getProject(String projectId, HttpSession session) {
        return restTemplate.getForObject(baseApiProjectUrl + normalize(projectId), ProjectModel.class);
    }

    @Override
    public String getFile(String projectId, String file, HttpSession session) {
        return restTemplate.getForObject(baseApiProjectUrl + normalize(projectId) + normalize(file), String.class);
    }

    @Override
    public Boolean isProjectExists(String projectId) {
         return HttpStatus.OK.equals(restTemplate.exchange(baseApiProjectUrl + normalize(projectId), HttpMethod.HEAD, null, ProjectModel.class).getStatusCode());
    }

    @Override
    public void putFiles(String projectId, List<FileModel> files, HttpSession session) {
        HttpEntity<List<FileModel>> requestUpdate = new HttpEntity<>(files);
        restTemplate.put(baseApiProjectUrl + normalize(projectId), requestUpdate);
    }
}
