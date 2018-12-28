package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.MetadataEnvironment;

public interface MetadataEnvironmentAware {
    void setEnvironment(MetadataEnvironment environment);
}
