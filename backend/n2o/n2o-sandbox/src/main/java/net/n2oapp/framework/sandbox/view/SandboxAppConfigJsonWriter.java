package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.http.HttpSession;

public class SandboxAppConfigJsonWriter extends AppConfigJsonWriter {

    private String projectId;
    private SandboxRestClient restClient;
    private HttpSession session;

    public SandboxAppConfigJsonWriter(String projectId, SandboxRestClient restClient, HttpSession session) {
        this.projectId = projectId;
        this.restClient = restClient;
        this.session = session;
    }

    @Override
    protected void readOverrideResource(PathMatchingResourcePatternResolver r, N2oConfigBuilder<AppConfig> configBuilder) {
        String file = restClient.getFile(projectId, "config.json", session);
        if (file != null)
            configBuilder.read(file);
    }
}
