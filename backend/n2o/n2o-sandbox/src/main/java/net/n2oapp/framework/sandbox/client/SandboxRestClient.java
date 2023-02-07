package net.n2oapp.framework.sandbox.client;

import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;

import java.util.List;

/**
 * Клиент для отправки запросов на ApiController
 */
public interface SandboxRestClient {

    ProjectModel getProject(String projectId);

    String getFile(String projectId, String file);

    boolean isProjectExists(String projectId);

    void putFiles(String projectId, List<FileModel> files);
}
