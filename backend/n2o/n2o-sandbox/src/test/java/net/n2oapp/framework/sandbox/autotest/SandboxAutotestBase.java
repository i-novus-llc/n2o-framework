package net.n2oapp.framework.sandbox.autotest;

import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.compile.query.MongodbEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.properties.reader.PropertiesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Базовый класс для автотестов примеров сендбокса
 */
@SpringBootTest(classes = {SandboxAutotestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandboxAutotestBase extends AutoTestBase {

    @Value("${n2o.sandbox.project-id}")
    private String projectId;

    @Autowired
    private SandboxRestClient restClient;

    @Autowired
    private SandboxPropertyResolver propertyResolver;

    private Map<String, String> runtimeProperties = new HashMap<>();

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        MockHttpSession session = new MockHttpSession();

        SimplePropertyResolver defaultPropertyResolver = new SimplePropertyResolver(PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties"));
        propertyResolver.configure(defaultPropertyResolver,
                runtimeProperties, initProperties(session));
        ((N2oEnvironment) builder.getEnvironment()).setSystemProperties(propertyResolver);

        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack(), new N2oAllIOPack(),
                new N2oApplicationPack(), new N2oLoadersPack());
        builder.scanners(
                new JavaInfoScanner((N2oDynamicMetadataProviderFactory) builder.getEnvironment().getDynamicMetadataProviderFactory()),
                new ProjectFileScanner(projectId, session,
                        builder.getEnvironment().getSourceTypeRegister(), restClient));
        builder.loaders(new ProjectFileLoader(builder.getEnvironment().getNamespaceReaderFactory()));
        builder.transformers(new TestEngineQueryTransformer(), new MongodbEngineQueryTransformer());
        builder.scan();
    }

    protected void addRuntimeProperty(String key, String value) {
        runtimeProperties.put(key, value);
    }

    private String initProperties(MockHttpSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append(Optional.ofNullable(restClient.getFile(projectId, "application.properties", session)).orElse(""));
        builder.append("\n");
        builder.append(Optional.ofNullable(restClient.getFile(projectId, "user.properties", session)).orElse(""));
        return builder.toString();
    }
}
