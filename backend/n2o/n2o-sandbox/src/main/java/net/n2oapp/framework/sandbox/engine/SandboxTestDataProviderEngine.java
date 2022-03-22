package net.n2oapp.framework.sandbox.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
//        String projectId = ThreadLocalProjectId.getProjectId();
//        TemplateModel template = templatesHolder.getTemplateModel(projectId);
//        if (template == null)
//            super.updateFile(filename);
//        else {
//            ProjectModel project = ProjectUtil.getFromSession(session, projectId);
//            if (project == null) {
//                project = ProjectUtil.initTemplateSessionProject(template);
//                project.setFiles(ProjectUtil.findResources(template.getTemplateId()));
//            }
//            try {
//                String mapAsJson = super.getObjectMapper().writeValueAsString(getRepositoryData(filename));
//                project.getFiles()
//                        .stream()
//                        .filter(f -> filename.equals(f.getFile()))
//                        .findFirst()
//                        .ifPresent(j -> j.setSource(mapAsJson));
//                ProjectUtil.putInSession(projectId, project, session);
//            } catch (JsonProcessingException e) {
//                throw new N2oException(e);
//            }
//        }
    }

    @Override
    protected synchronized List<DataSet> getData(N2oTestDataProvider invocation) {
        String projectId = ThreadLocalProjectId.getProjectId();
//        ProjectModel project = ProjectUtil.getFromSession(session, projectId);
//        if (project != null) {
//            initRepository(invocation);
//        }

        return super.getData(invocation);
    }

    @Override
    protected InputStream getResourceInputStream(N2oTestDataProvider invocation) {
        String projectId = ThreadLocalProjectId.getProjectId();
//        ProjectModel project = ProjectUtil.getFromSession(session, projectId);
//        if (project != null) {
//            FileModel jsonFile = project.getFiles().stream()
//                    .filter(f -> f.getFile().equals(invocation.getFile()))
//                    .findFirst()
//                    .get();
//            return new ByteArrayInputStream(jsonFile.getSource().getBytes());
//        }

        return new ByteArrayInputStream(restClient.getFile(projectId, invocation.getFile(), session).getBytes());
    }

    @Override
    protected String richKey(String key) {
        String projectId = ThreadLocalProjectId.getProjectId();
        return projectId + "/" + key;
    }
}
