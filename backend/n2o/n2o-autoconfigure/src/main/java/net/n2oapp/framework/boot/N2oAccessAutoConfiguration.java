package net.n2oapp.framework.boot;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.integration.N2oSecurityModule;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.access.mock.PermissionApiMock;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(SecurityProvider.class)
@ComponentScan(basePackages = "net.n2oapp.framework.access", lazyInit = true)
public class N2oAccessAutoConfiguration {
    @Value("${n2o.access.strict_filtering:false}")
    private Boolean strictFiltering;


    @Bean
    public SecurityProvider securityProvider(PermissionApi permissionApi) {
        return new SecurityProvider(permissionApi, strictFiltering);
    }

    @Bean
    public N2oSecurityModule n2oSecurityModule(PermissionApi permissionApi){
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, strictFiltering);
        return new N2oSecurityModule(securityProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public PermissionApi permissionApi() {
        return new PermissionApiMock();
    }

    @Bean
    public MetadataPack<N2oApplicationBuilder> accessMetadataPack () {
        return (b) -> b.types(new MetaType("access", N2oAccessSchema.class));
    }

    @Bean
    public MetaType accessType() {
        return new MetaType("access", N2oAccessSchema.class);
    }


}
