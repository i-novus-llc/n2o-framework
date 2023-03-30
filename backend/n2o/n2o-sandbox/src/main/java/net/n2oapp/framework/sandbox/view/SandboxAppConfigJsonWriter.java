package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SandboxAppConfigJsonWriter extends AppConfigJsonWriter {

    private String projectId;
    private SandboxRestClient restClient;

    public SandboxAppConfigJsonWriter(String projectId, SandboxRestClient restClient) {
        this.projectId = projectId;
        this.restClient = restClient;
    }

    @Override
    protected void readOverrideResource(PathMatchingResourcePatternResolver r, N2oConfigBuilder<AppConfig> configBuilder) {
        String file = restClient.getFile(projectId, "config.json");
        if (file != null)
            configBuilder.read(file);
    }
}
