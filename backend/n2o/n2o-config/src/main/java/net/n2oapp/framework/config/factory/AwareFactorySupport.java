package net.n2oapp.framework.config.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AwareFactorySupport {
    public static void enrich(Object service, MetadataEnvironment environment) {
        if (environment != null && service instanceof MetadataEnvironmentAware environmentAware)
            environmentAware.setEnvironment(environment);
    }

}
