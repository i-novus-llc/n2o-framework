package net.n2oapp.framework.ui.controller;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.ui.controller.query.SimpleDefaultValuesController;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DefaultValuesControllerTest {

    private N2oApplicationBuilder builder;

    @Before
    public void setUp() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap());
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("n2o_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        environment.setMessageSource(new MessageSourceAccessor(messageSource));
        OverrideProperties properties = PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties");
        properties.put("n2o.engine.mapper", "spel");
        environment.setSystemProperties(new SimplePropertyResolver(properties));
        builder = new N2oApplicationBuilder(environment);
        configure(builder);
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
    }

    private void configure(N2oApplicationBuilder builder) {
        builder.packs(
                new N2oSourceTypesPack(),
                new N2oDataProvidersPack(),
                new N2oQueriesPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oOperationsPack()
        );
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }

    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> compile(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read().compile();
    }

    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> createPipelineForQuery() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/ui/controller/testDefaults.query.xml",
                "net/n2oapp/framework/ui/controller/testDefaults.page.xml"
        );
        pipeline.get(new PageContext("testDefaults"));
        return pipeline;
    }

    @Test
    public void testUniqueSelection() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipelineForQuery();
        Map<String, String[]> params = new HashMap<>();
        params.put("id", new String[]{"2"});
        GetDataResponse response = testQuery("/testDefaults/main", pipeline, params);
        assertThat(response.getList().size(), is(1));
        assertThat(response.getList().get(0).size(), is(2));
        assertThat(response.getList().get(0).get("id"), is(2L));
        assertThat(response.getList().get(0).get("name"), is("testName2"));
    }

    private GetDataResponse testQuery(String path,
                                      ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline,
                                      Map<String, String[]> params) {
        N2oInvocationFactory invocationFactory = Mockito.mock(N2oInvocationFactory.class);
        TestDataProviderEngine testDataProviderEngine = new TestDataProviderEngine();
        testDataProviderEngine.setResourceLoader(new DefaultResourceLoader());
        Mockito.when(invocationFactory.produce(Mockito.any(Class.class))).thenReturn(testDataProviderEngine);
        ContextEngine contextEngine = Mockito.mock(ContextEngine.class);
        UserContext userContext = new UserContext(contextEngine);
        ContextProcessor contextProcessor = new ContextProcessor(userContext);
        N2oQueryProcessor queryProcessor = new N2oQueryProcessor(invocationFactory, new N2oQueryExceptionHandler());
        N2oEnvironment env = new N2oEnvironment();
        env.setContextProcessor(contextProcessor);
        queryProcessor.setEnvironment(env);
        N2oSubModelsProcessor subModelsProcessor = Mockito.mock(N2oSubModelsProcessor.class);
        Mockito.doNothing().when(subModelsProcessor);
        DataProcessingStack dataProcessingStack = Mockito.mock(SpringDataProcessingStack.class);

        SimpleDefaultValuesController valuesController = new SimpleDefaultValuesController(dataProcessingStack, queryProcessor,
                subModelsProcessor, null, null);
        Map<String, Object> map = new HashMap<>();
        map.put("SimpleDefaultValuesController", valuesController);

        N2oRouter router = new N2oRouter(builder.getEnvironment(), pipeline);
        N2oControllerFactory factory = new N2oControllerFactory(map);
        factory.setEnvironment(builder.getEnvironment());
        DataController controller = new DataController(factory, builder.getEnvironment(), router);
        return controller.getData(path, params, userContext);
    }
}
