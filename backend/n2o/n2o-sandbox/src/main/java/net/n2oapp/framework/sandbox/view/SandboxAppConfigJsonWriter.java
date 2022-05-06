package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    public N2oConfigBuilder<AppConfig> build() {
        N2oConfigBuilder<AppConfig> configBuilder = new N2oConfigBuilder<>(new AppConfig(),
                getObjectMapper(), getPropertyResolver(), getContextProcessor());
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        try {
            for (Resource resource : r.getResources(getPath())) {
                configBuilder.read(resource);
            }
            String file = restClient.getFile(projectId, "config.json", session);
            if (file != null)
                configBuilder.read(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        getConfigs().forEach(configBuilder::read);
        return configBuilder;
    }
}
