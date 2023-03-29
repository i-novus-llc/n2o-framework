package net.n2oapp.framework.sandbox.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Тестовый провайдер данных для чтения/изменения json при работе с sandbox
 */
@Component
public class SandboxTestDataProviderEngine extends TestDataProviderEngine {

    @Autowired
    private SandboxRestClient restClient;

    @Override
    public Object invoke(N2oTestDataProvider invocation, Map<String, Object> inParams) {
        return super.invoke(invocation, inParams);
    }

    @Override
    protected synchronized List<DataSet> getData(N2oTestDataProvider invocation) {
        if (invocation.getFile() == null)
            return new ArrayList<>();
        boolean isInit = getRepositoryData(invocation.getFile()) == null;
        if (isInit || !isReadonly()) {
            initRepository(invocation);
        }

        return getRepository().get(richKey(invocation.getFile()));
    }

    @Override
    protected void updateFile(String filename) {
        String projectId = ThreadLocalProjectId.getProjectId();
        try {
            String mapAsJson = super.getObjectMapper().writeValueAsString(getRepositoryData(filename));
            FileModel fileModel = new FileModel();
            fileModel.setFile(filename);
            fileModel.setSource(mapAsJson);
            restClient.putFiles(projectId, Collections.singletonList(fileModel));
        } catch (JsonProcessingException e) {
            throw new N2oException(e);
        }
    }

    @Override
    protected InputStream getResourceInputStream(N2oTestDataProvider invocation) throws IOException {
        try {
            String projectId = ThreadLocalProjectId.getProjectId();
            String response = restClient.getFile(projectId, invocation.getFile());
            if (response != null)
                return new ByteArrayInputStream(response.getBytes());
            throw new FileNotFoundException(invocation.getFile());
        } catch (HttpClientErrorException.NotFound e) {
            ClassPathResource classPathResource = new ClassPathResource(invocation.getFile());
            if (classPathResource.exists()) {
                return classPathResource.getInputStream();
            }
            throw new FileNotFoundException(invocation.getFile());
        }
    }

    @Override
    protected String richKey(String key) {
        String projectId = ThreadLocalProjectId.getProjectId();
        return projectId + "/" + key;
    }
}
