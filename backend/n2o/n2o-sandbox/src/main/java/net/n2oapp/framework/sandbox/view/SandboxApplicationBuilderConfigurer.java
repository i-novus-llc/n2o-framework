package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.SecurityPageBinder;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.boot.ApplicationBuilderConfigurer;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.MongodbEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.scanner.DefaultXmlInfoScanner;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import org.springframework.beans.factory.annotation.Autowired;

public class SandboxApplicationBuilderConfigurer implements ApplicationBuilderConfigurer {

    @Autowired
    private SecurityProvider securityProvider;

    @Override
    public void configure(N2oApplicationBuilder builder) {

        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack(), new N2oAllIOPack(), new N2oApplicationPack(),
                new N2oLoadersPack(), new N2oOperationsPack(), new N2oSourceTypesPack(),
                new AccessSchemaPack(), new N2oAllValidatorsPack());
        builder.scanners(new DefaultXmlInfoScanner(),
                new XmlInfoScanner("classpath:META-INF/conf/*.xml"),
                new JavaInfoScanner((N2oDynamicMetadataProviderFactory) builder.getEnvironment().getDynamicMetadataProviderFactory()));
        builder.binders(new SecurityPageBinder(securityProvider));
        builder.loaders(new ProjectFileLoader(builder.getEnvironment().getNamespaceReaderFactory()));

        builder.transformers(new TestEngineQueryTransformer(), new MongodbEngineQueryTransformer());
    }
}
