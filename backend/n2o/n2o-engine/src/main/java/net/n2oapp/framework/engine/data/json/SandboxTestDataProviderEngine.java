package net.n2oapp.framework.engine.data.json;

import net.n2oapp.framework.engine.data.ThreadLocalProjectId;

public class SandboxTestDataProviderEngine extends TestDataProviderEngine {

    @Override
    public String getResourcePath() {
       return getPathOnDisk() + "/" + ThreadLocalProjectId.getProjectId();
    }
}