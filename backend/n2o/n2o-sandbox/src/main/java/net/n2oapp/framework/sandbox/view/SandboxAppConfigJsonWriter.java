package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SandboxAppConfigJsonWriter extends AppConfigJsonWriter {

    private String projectId;
    private FileStorage fileStorage;

    public SandboxAppConfigJsonWriter(String projectId, FileStorage fileStorage) {
        this.projectId = projectId;
        this.fileStorage = fileStorage;
    }

    @Override
    protected void readOverrideResource(PathMatchingResourcePatternResolver r, N2oConfigBuilder<AppConfig> configBuilder) {
        String file = fileStorage.getFileContent(projectId, "config.json");
        if (file != null)
            configBuilder.read(file);
    }
}
