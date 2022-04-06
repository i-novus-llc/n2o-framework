package net.n2oapp.framework.sandbox.autotest;

import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.MongodbEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.scanner.DefaultXmlInfoScanner;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest(classes = {SandboxAutotestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandboxAutotestBase extends AutoTestBase {

    @Value("${n2o.sandbox.project-id}")
    private String projectId;

    @Autowired
    private SandboxRestClient restClient;

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack(), new N2oAllIOPack(),
                new N2oApplicationPack(), new N2oLoadersPack());
        builder.scanners(new DefaultXmlInfoScanner(),
                new JavaInfoScanner((N2oDynamicMetadataProviderFactory) builder.getEnvironment().getDynamicMetadataProviderFactory()),
                new ProjectFileScanner(projectId, new MockHttpSession(),
                        builder.getEnvironment().getSourceTypeRegister(), restClient));
        builder.loaders(new ProjectFileLoader(builder.getEnvironment().getNamespaceReaderFactory()));
        builder.transformers(new TestEngineQueryTransformer(), new MongodbEngineQueryTransformer());
        builder.scan();
    }
}
