package net.n2oapp.framework.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oValidationException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.processing.N2oModule;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DataControllerTest {

    private N2oApplicationBuilder builder;

    private static boolean setUpIsDone = false;

    private static void setUpStaticProperties(PropertyResolver propertyResolver) {
        if (setUpIsDone) return;
        StaticProperties staticProperties = new StaticProperties();
        staticProperties.setPropertyResolver(propertyResolver);
        setUpIsDone = true;
    }

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
        SimplePropertyResolver propertyResolver = new SimplePropertyResolver(properties);
        setUpStaticProperties(propertyResolver);
        builder = new N2oApplicationBuilder(environment);
        configure(builder);
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
    }

    private void configure(N2oApplicationBuilder builder) {
        builder.packs(new N2oSourceTypesPack(),
                new N2oDataProvidersPack(),
                new N2oObjectsPack(), new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(),
                new N2oOperationsPack());
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }

    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> compile(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read().compile();
    }

    /**
     * Тест whitelist + inline condition, mandatory, constraint + control inline валидации
     */
    @Test
    public void test() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id1", null);
        body.put("id2", null);
        body.put("id3", null);
        body.put("id4", null);
        body.put("id5", null);
        body.put("id6", null);
        body.put("id7", null);
        body.put("id8", null);
        body.put("id9", null);
        body.put("id10", null);
        body.put("id11", null);
        body.put("id12", null);
        body.put("id13", null);

        SetDataResponse result = testOperation("/page/widget/:testPage_main_id/create", pipeline, params, body);
        assertThat(result.getMeta().getMessages().getFields().size(), is(8));
        assertThat(result.getMeta().getMessages().getFields().get("id1").getField(), is("id1"));
        assertThat(result.getMeta().getMessages().getFields().get("id2").getField(), is("id2"));
        assertThat(result.getMeta().getMessages().getFields().get("id4").getField(), is("id4"));
        assertThat(result.getMeta().getMessages().getFields().get("id5").getField(), is("id5"));
        assertThat(result.getMeta().getMessages().getFields().get("id6").getField(), is("id6"));
        assertThat(result.getMeta().getMessages().getFields().get("id9").getField(), is("id9"));
        assertThat(result.getMeta().getMessages().getFields().get("id10").getField(), is("id10"));
        assertThat(result.getMeta().getMessages().getFields().get("id11").getField(), is("id11"));
    }

    /**
     * Проверяется, что <validations/> в операции включает все валидации объекта, где enabled == true
     */
    @Test
    public void testAllEnabledValidations() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id1", null);
        body.put("id2", null);
        body.put("id3", null);
        body.put("id4", null);
        body.put("id5", null);
        body.put("id6", null);
        body.put("id7", null);
        body.put("id8", null);
        body.put("id9", null);
        body.put("id10", null);
        body.put("id11", null);
        body.put("id13", null);

        SetDataResponse result = testOperation("/page/widget/:testPage_main_id/create2", pipeline, params, body);
        assertThat(result.getMeta().getMessages().getFields().size(), is(6));
        assertThat(result.getMeta().getMessages().getFields().get("id2").getField(), is("id2"));
        assertThat(result.getMeta().getMessages().getFields().get("id3").getField(), is("id3"));
        assertThat(result.getMeta().getMessages().getFields().get("id4").getField(), is("id4"));
        assertThat(result.getMeta().getMessages().getFields().get("id9").getField(), is("id9"));
        assertThat(result.getMeta().getMessages().getFields().get("id11").getField(), is("id11"));
    }


    /**
     * Проверяется, что после danger валидации не валидируются warning, success, info
     */
    @Test
    public void testDangerSeverity() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id1", null);
        body.put("id2", null);
        body.put("id3", null);
        body.put("id4", null);
        body.put("id5", null);
        body.put("id6", null);
        body.put("id7", null);
        body.put("id8", null);
        body.put("id9", null);
        body.put("id10", null);
        body.put("id11", null);
        body.put("id13", null);

        try {
            testOperation("/page/widget/:testPage_main_id/create3", pipeline, params, body);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getAlertKey(), is("page_main"));
            assertThat(e.getSeverity(), is(SeverityType.danger));
            assertThat(e.getMessages().size(), is(1));
            assertThat(e.getMessages().get(0).getFieldId(), is("id7"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id7IsNotNull"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id7IsNotNull"));
        }
    }

    /**
     * Проверяется black-list в валидациях операции
     */
    @Test
    public void testBlackList() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id1", null);
        body.put("id2", null);
        body.put("id3", null);
        body.put("id4", null);
        body.put("id5", null);
        body.put("id6", null);
        body.put("id7", null);
        body.put("id8", null);
        body.put("id9", null);
        body.put("id10", null);
        body.put("id11", null);
        body.put("id13", null);

        SetDataResponse result = testOperation("/page/widget/:testPage_main_id/create4", pipeline, params, body);
        assertThat(result.getMeta().getMessages().getFields().size(), is(3));
        assertThat(result.getMeta().getMessages().getFields().containsKey("id9"), is(true));
        assertThat(result.getMeta().getMessages().getFields().containsKey("id10"), is(true));
        assertThat(result.getMeta().getMessages().getFields().containsKey("id11"), is(true));
    }

    /**
     * Проверяется, что если поле отсутствует в датасете, но оно обязательно на форме,
     * то сработает Mandatory валидация для этого поля
     */
    @Test
    public void testRequiredControl() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id1", null);

        try {
            testOperation("/pageWithRequiredField/widget/:testPageWithRequiredField_main_id/create", pipeline, params, body);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getAlertKey(), is("pageWithRequiredField_main"));
            assertThat(e.getMessages().size(), is(1));
            assertThat(e.getMessages().get(0).getFieldId(), is("id1"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id1"));
        }
    }

    /**
     * Проверяется, что при заданиии в операции параметра со свойством required=true,
     * соответствующее ему поле валидируется Mandatory валидацией
     */
    @Test
    public void testRequiredInParam() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();

        body.put("id13", null);

        try {
            testOperation("/page/widget/:testPage_main_id/create5", pipeline, params, body);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getAlertKey(), is("page_main"));
            assertThat(e.getMessages().size(), is(1));
            assertThat(e.getMessages().get(0).getFieldId(), is("id13"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id13"));
        }
    }

    /**
     * Inline валидации полей с visible = false или не выполняющимся visibility не срабатывают
     */
    @Test
    public void testFieldVisibility() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();
        body.put("id1", null);

        try {
            testOperation("/testFieldVisibility/widget/:testFieldVisibility_main_id/create6", pipeline, params, body);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getAlertKey(), is("testFieldVisibility_main"));
            assertThat(e.getMessages().size(), is(2));
            assertThat(e.getMessages().get(0).getFieldId(), is("id1"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id1Required"));
            assertThat(e.getMessages().get(1).getFieldId(), is("id3"));
            assertThat(e.getMessages().get(1).getValidationId(), is("id3Required"));
        }


    }

    /**
     * Проверка Mandatory на списковых котролах.
     * Валидация должна фейлиться на пустых списках и мапах.
     */
    @Test
    public void testRequiredListControl() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();
        body.put("id1", new ArrayList<>());
        body.put("id2", new HashMap<>());

        try {
            testOperation("/testListControl/widget/:testListControl_main_id/create7", pipeline, params, body);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getAlertKey(), is("testListControl_main"));
            assertThat(e.getMessages().size(), is(2));
            assertThat(e.getMessages().get(0).getFieldId(), is("id1"));
            assertThat(e.getMessages().get(0).getValidationId(), is("id1"));
            assertThat(e.getMessages().get(1).getFieldId(), is("id2"));
            assertThat(e.getMessages().get(1).getValidationId(), is("id2Required"));
        }
    }


    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> createPipeline() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/ui/controller/testObject.object.xml",
                "net/n2oapp/framework/ui/controller/testListControlValidation.page.xml",
                "net/n2oapp/framework/ui/controller/testPageWithRequiredField.page.xml",
                "net/n2oapp/framework/ui/controller/testFieldVisibility.page.xml",
                "net/n2oapp/framework/ui/controller/testPage.page.xml");
        pipeline.get(new PageContext("testPage"));
        pipeline.get(new PageContext("testPageWithRequiredField"));
        pipeline.get(new PageContext("testFieldVisibility"));
        pipeline.get(new PageContext("testListControlValidation"));
        return pipeline;
    }

    private SetDataResponse testOperation(String path,
                                          ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline,
                                          Map<String, String[]> params,
                                          DataSet body) {


        N2oInvocationFactory invocationFactory = Mockito.mock(N2oInvocationFactory.class);
        TestDataProviderEngine testDataProviderEngine = new TestDataProviderEngine();
        testDataProviderEngine.setResourceLoader(new DefaultResourceLoader());

        Mockito.when(invocationFactory.produce(Mockito.any(Class.class))).thenReturn(testDataProviderEngine);

        N2oInvocationProcessor invocationProcessor = new N2oInvocationProcessor(invocationFactory, null, null);

        N2oValidationModule validationModule = new N2oValidationModule(new ValidationProcessor(invocationProcessor));
        Map<String, N2oModule> moduleMap = new HashMap<>();
        moduleMap.put("validationModule", validationModule);
        ObjectMapper mapper = new ObjectMapper();
        DomainProcessor domainProcessor = new DomainProcessor(mapper, "dd.MM.yyyy");

        N2oOperationProcessor operationProcessor = new N2oOperationProcessor(invocationProcessor);

        ApplicationContext context = Mockito.mock(ApplicationContext.class);
        Mockito.when(context.getBeansOfType(N2oModule.class)).thenReturn(moduleMap);
        DataProcessingStack dataProcessingStack = new SpringDataProcessingStack();
        ((SpringDataProcessingStack) dataProcessingStack).setApplicationContext(context);


        N2oRouter router = new N2oRouter(builder.getEnvironment().getRouteRegister(), pipeline);
        ContextEngine contextEngine = Mockito.mock(ContextEngine.class);

        Map<String, Object> map = new HashMap<>();
        OperationController operationController = new OperationController(dataProcessingStack, domainProcessor, operationProcessor);
        map.put("operationController", operationController);

        N2oControllerFactory factory = new N2oControllerFactory(map);
        factory.setEnvironment(builder.getEnvironment());

        DataController controller = new DataController(factory, mapper, router, builder.getEnvironment());
//        controller.setErrorMessageBuilder(new ErrorMessageBuilder(new MessageSourceAccessor(new ResourceBundleMessageSource())));
        return controller.setData(path, params, body, new UserContext(contextEngine));
    }

}
