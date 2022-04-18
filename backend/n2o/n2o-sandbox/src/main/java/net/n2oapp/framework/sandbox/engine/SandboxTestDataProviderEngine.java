package net.n2oapp.framework.sandbox.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.utils.ProjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Тестовый провайдер данных для чтения/изменения json при работе с sandbox
 */
@Component
public class SandboxTestDataProviderEngine extends TestDataProviderEngine {

    @Autowired
    private HttpSession session;
    @Autowired
    private SandboxRestClient restClient;

    @Override
    public Object invoke(N2oTestDataProvider invocation, Map<String, Object> inParams) {
        return super.invoke(invocation, inParams);
    }

    @Override
    protected void updateFile(String filename) {
        String projectId = ThreadLocalProjectId.getProjectId();
        try {
            String mapAsJson = super.getObjectMapper().writeValueAsString(getRepositoryData(filename));
            FileModel fileModel = new FileModel();
            fileModel.setFile(filename);
            fileModel.setSource(mapAsJson);
            restClient.putFiles(projectId, Collections.singletonList(fileModel), session);
        } catch (JsonProcessingException e) {
            throw new N2oException(e);
        }
    }

    @Override
    protected synchronized List<DataSet> getData(N2oTestDataProvider invocation) {
        String projectId = ThreadLocalProjectId.getProjectId();
        ProjectModel project = ProjectUtil.getFromSession(session, projectId);
        if (project != null) {
            initRepository(invocation);
        }

        return super.getData(invocation);
    }

    @Override
    protected InputStream getResourceInputStream(N2oTestDataProvider invocation) {
        String projectId = ThreadLocalProjectId.getProjectId();
        return new ByteArrayInputStream(restClient.getFile(projectId, invocation.getFile(), session).getBytes());
    }

    @Override
    protected String richKey(String key) {
        String projectId = ThreadLocalProjectId.getProjectId();
        return projectId + "/" + key;
    }
}
