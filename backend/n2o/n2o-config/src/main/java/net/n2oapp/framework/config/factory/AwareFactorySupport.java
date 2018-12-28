package net.n2oapp.framework.config.factory;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;

public class AwareFactorySupport {
    public static void enrich(Object service, MetadataEnvironment environment) {
        if (environment != null && service instanceof MetadataEnvironmentAware)
            ((MetadataEnvironmentAware) service).setEnvironment(environment);
    }

}
