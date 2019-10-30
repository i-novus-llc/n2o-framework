package net.n2oapp.framework.access;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.integration.N2oSecurityModule;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.access.mock.PermissionApiMock;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.n2oapp.framework.access", lazyInit = true)
public class AccessConfiguration {

    @Value("${n2o.access.schema.id}")
    private String accessSchemaId;

    @Value("${n2o.access.admins}")
    private String accessAdmins;

    @Value("${n2o.access.strict_filtering:false}")
    private Boolean strictFiltering;

    @Value("${n2o.access.N2oObjectAccessPoint.default:false}")
    private Boolean defaultObjectAccess;

    @Value("${n2o.access.N2oReferenceAccessPoint.default:true}")
    private Boolean defaultReferenceAccess;

    @Value("${n2o.access.N2oPageAccessPoint.default:true}")
    private Boolean defaultPageAccess;

    @Value("${n2o.access.N2oUrlAccessPoint.default:true}")
    private Boolean defaultUrlAccess;

    @Value("${n2o.access.N2oColumnAccessPoint.default:true}")
    private Boolean defaultColumnAccess;

    @Value("${n2o.access.N2oFilterAccessPoint.default:true}")
    private Boolean defaultFilterAccess;

    @Bean
    public SecurityProvider securityProvider(PermissionApi permissionApi) {
        return new SecurityProvider(permissionApi, strictFiltering);
    }

    @Bean
    public N2oSecurityModule n2oSecurityModule(PermissionApi permissionApi){
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, strictFiltering);
        N2oSecurityModule n2oSecurityModule = new N2oSecurityModule(securityProvider);
        n2oSecurityModule.setAfterAll(true);
        return n2oSecurityModule;
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
