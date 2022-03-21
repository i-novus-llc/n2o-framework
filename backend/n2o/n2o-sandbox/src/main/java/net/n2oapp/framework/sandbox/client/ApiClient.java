package net.n2oapp.framework.sandbox.client;

import net.n2oapp.framework.sandbox.client.model.ProjectModel;

import javax.servlet.http.HttpSession;

public interface ApiClient {

    ProjectModel getProject(String projectId, HttpSession session);

    String getFile(String projectId, String file);

    Boolean isProjectNotExists(String projectId);
}
