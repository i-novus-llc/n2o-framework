package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.N2oOperationExceptionHandler;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.ui.controller.action.OperationController;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class DataControllerTest extends DataControllerTestBase {

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

        SetDataResponse result = testOperation("/page/widget/create", pipeline, params, body);
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

        SetDataResponse result = testOperation("/page/widget/create2", pipeline, params, body);
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

        SetDataResponse response = testOperation("/page/widget/create3", pipeline, params, body);
        assertThat(response.getMeta().getMessages().getForm(), is("page_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(1));
        assertThat(response.getMeta().getMessages().getFields().get("id7").getField(), is("id7"));
        assertThat(response.getMeta().getMessages().getFields().get("id7").getSeverity(), is(SeverityType.danger.getId()));
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

        SetDataResponse result = testOperation("/page/widget/create4", pipeline, params, body);
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

        SetDataResponse response = testOperation("/pageWithRequiredField/widget/create", pipeline, params, body);
        assertThat(response.getMeta().getMessages().getForm(), is("pageWithRequiredField_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(1));
        assertThat(response.getMeta().getMessages().getFields().get("id1").getField(), is("id1"));
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

        SetDataResponse response = testOperation("/page/widget/create5", pipeline, params, body);
        assertThat(response.getMeta().getMessages().getForm(), is("page_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(1));
        assertThat(response.getMeta().getMessages().getFields().get("id13").getField(), is("id13"));
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

        SetDataResponse response = testOperation("/testFieldVisibility/widget/create6", pipeline, params, body);
        assertThat(response.getMeta().getMessages().getForm(), is("testFieldVisibility_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(2));
        assertThat(response.getMeta().getMessages().getFields().get("id1").getField(), is("id1"));
        assertThat(response.getMeta().getMessages().getFields().get("id3").getField(), is("id3"));


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

        SetDataResponse response = testOperation("/testListControl/widget/create7", pipeline, params, body);
        assertThat(response.getMeta().getMessages().getForm(), is("testListControl_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(2));
        assertThat(response.getMeta().getMessages().getFields().get("id1").getField(), is("id1"));
        assertThat(response.getMeta().getMessages().getFields().get("id2").getField(), is("id2"));
    }

    /**
     * Проверка operation in param.
     */
    @Test
    public void testOperationMapping() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();

        //Параметры не превращаются в body, но учитываются в in field
        Map<String, String[]> params = new HashMap<>();
        params.put("param1", new String[] {"value11"});
        params.put("param3", new String[] {"value33"});
        DataSet body = new DataSet();
        body.put("field1", "value1");
        body.put("field2", "value2");
        body.put("field3", "value3");

        SetDataResponse response = testOperation("/testOperationMapping/widget/create8", pipeline, params, body);
        assertThat(response.getData().size(), is(3));
        assertThat(response.getData().get("field1"), is("value11"));
        assertThat(response.getData().get("field3"), is("value33"));
        assertThat(response.getData().get("param1"), nullValue());
        assertThat(response.getData().get("param3"), nullValue());

        //Поля в body не считаются за параметры и не учитываются в in field
        params = new HashMap<>();
        body = new DataSet();
        body.put("param1", "value1");
        body.put("field2", "value2");
        body.put("param3", "value3");

        response = testOperation("/testOperationMapping/widget/create8", pipeline, params, body);
        assertThat(response.getData().get("field1"), nullValue());
        assertThat(response.getData().get("field3"), nullValue());

        //В in field нет значения по умолчанию для param, параметры похожие на in field id не превращаются в параметры
        params = new HashMap<>();
        params.put("field2", new String[] {"value22"});
        body = new DataSet();
        body.put("field1", "value1");
        body.put("field2", "value2");
        body.put("field3", "value3");

        response = testOperation("/testOperationMapping/widget/create8", pipeline, params, body);
        assertThat(response.getData().get("field2"), is("value2"));
    }


    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> createPipeline() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/ui/controller/testObject.object.xml",
                "net/n2oapp/framework/ui/controller/testListControlValidation.page.xml",
                "net/n2oapp/framework/ui/controller/testPageWithRequiredField.page.xml",
                "net/n2oapp/framework/ui/controller/testFieldVisibility.page.xml",
                "net/n2oapp/framework/ui/controller/testQuery.query.xml",
                "net/n2oapp/framework/ui/controller/testPage.page.xml",
                "net/n2oapp/framework/ui/controller/testOperationMapping.page.xml");
        pipeline.get(new PageContext("testPage"));
        pipeline.get(new PageContext("testPageWithRequiredField"));
        pipeline.get(new PageContext("testFieldVisibility"));
        pipeline.get(new PageContext("testListControlValidation"));
        pipeline.get(new PageContext("testOperationMapping"));
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

        N2oInvocationProcessor invocationProcessor = new N2oInvocationProcessor(invocationFactory);
        invocationProcessor.setEnvironment(builder.getEnvironment());

        N2oValidationModule validationModule = new N2oValidationModule(new ValidationProcessor(invocationProcessor));
        Map<String, DataProcessing> moduleMap = new HashMap<>();
        moduleMap.put("validationModule", validationModule);
        N2oOperationProcessor operationProcessor = new N2oOperationProcessor(invocationProcessor, new N2oOperationExceptionHandler());

        ApplicationContext context = Mockito.mock(ApplicationContext.class);
        Mockito.when(context.getBeansOfType(DataProcessing.class)).thenReturn(moduleMap);
        DataProcessingStack dataProcessingStack = new SpringDataProcessingStack();
        ((SpringDataProcessingStack) dataProcessingStack).setApplicationContext(context);


        N2oRouter router = new N2oRouter(builder.getEnvironment(), pipeline);
        ContextEngine contextEngine = Mockito.mock(ContextEngine.class);

        Map<String, Object> map = new HashMap<>();
        AlertMessageBuilder messageBuilder = new AlertMessageBuilder(builder.getEnvironment().getMessageSource(), null);
        OperationController operationController = new OperationController(dataProcessingStack, operationProcessor,
                messageBuilder, builder.getEnvironment());
        map.put("operationController", operationController);

        N2oControllerFactory factory = new N2oControllerFactory(map);
        factory.setEnvironment(builder.getEnvironment());

        DataController controller = new DataController(factory, builder.getEnvironment(), router);
        controller.setMessageBuilder(messageBuilder);
        return controller.setData(path, params , null, body, new UserContext(contextEngine));
    }

}
