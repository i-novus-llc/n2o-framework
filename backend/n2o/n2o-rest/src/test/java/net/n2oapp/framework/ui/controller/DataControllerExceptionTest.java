package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oValidationException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.exception.ValidationMessage;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.N2oOperationExceptionHandler;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class DataControllerExceptionTest extends DataControllerTestBase {


    @Test
    public void testSetData() {
        DataProcessingStack dataProcessingStack = mock(DataProcessingStack.class);
        Exception e = new N2oException("Message");

        doThrow(e).when(dataProcessingStack).processAction(any(ActionRequestInfo.class), any(ActionResponseInfo.class), any(DataSet.class));
        DataController controller = buildController(dataProcessingStack);
        SetDataResponse response = controller.setData("/page/create", null, null, new DataSet(), null);

        assertThat(response.getMeta().getAlert().getAlertKey(), is("page_main"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), is("danger"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getStacktrace().get(0), is("net.n2oapp.framework.api.exception.N2oException: Message"));

        List<ValidationMessage> messages = new ArrayList<>();
        messages.add(new ValidationMessage("message1", "field1", "validation1"));
        messages.add(new ValidationMessage("message2", "field2", "validation2"));
        e = new N2oValidationException("Validation exception", "widget", messages, "messageForm");
        doThrow(e).when(dataProcessingStack).processAction(any(ActionRequestInfo.class), any(ActionResponseInfo.class), any(DataSet.class));
        response = controller.setData("/page/create", null, null, new DataSet(), null);

        assertThat(response.getMeta().getMessages().getForm(), is("page_main"));
        assertThat(response.getMeta().getMessages().getFields().size(), is(2));

        assertThat(response.getMeta().getMessages().getFields().get("field1").getText(), is("message1"));
        assertThat(response.getMeta().getMessages().getFields().get("field1").getField(), is("field1"));

        assertThat(response.getMeta().getMessages().getFields().get("field2").getText(), is("message2"));
        assertThat(response.getMeta().getMessages().getFields().get("field2").getField(), is("field2"));
    }

    @Test
    public void testGetData() {
        DataProcessingStack dataProcessingStack = mock(DataProcessingStack.class);

        Exception e = new N2oException("Message");
        doThrow(e).when(dataProcessingStack).processQuery(any(QueryRequestInfo.class), any(QueryResponseInfo.class));
        DataController controller = buildController(dataProcessingStack);
        GetDataResponse response = controller.getData("/page/main", null, null);
        assertThat(response.getMeta().getAlert().getMessages().get(0).getSeverity(), is(SeverityType.danger.toString()));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getStacktrace().get(0), is("net.n2oapp.framework.api.exception.N2oException: Message"));

        List<ValidationMessage> messages = new ArrayList<>();
        messages.add(new ValidationMessage("message1", "field1", "validation1"));
        messages.add(new ValidationMessage("message2", "field2", "validation2"));
        e = new N2oValidationException("Validation exception", "widget", messages, "messageForm");
        doThrow(e).when(dataProcessingStack).processQuery(any(QueryRequestInfo.class), any(QueryResponseInfo.class));
        controller = buildController(dataProcessingStack);
        response = controller.getData("/page/main", null, null);

//        assertThat(response.getMeta().getMessages().getForm(), is("page_main"));//todo NNO-7304
        assertThat(response.getMeta().getMessages().getFields().size(), is(2));

        assertThat(response.getMeta().getMessages().getFields().get("field1").getText(), is("message1"));
        assertThat(response.getMeta().getMessages().getFields().get("field1").getField(), is("field1"));

        assertThat(response.getMeta().getMessages().getFields().get("field2").getText(), is("message2"));
        assertThat(response.getMeta().getMessages().getFields().get("field2").getField(), is("field2"));

    }

    private DataController buildController(DataProcessingStack dataProcessingStack) {
        N2oInvocationFactory invocationFactory = Mockito.mock(N2oInvocationFactory.class);
        TestDataProviderEngine testDataProviderEngine = new TestDataProviderEngine();
        testDataProviderEngine.setResourceLoader(new DefaultResourceLoader());

        Mockito.when(invocationFactory.produce(Mockito.any(Class.class))).thenReturn(testDataProviderEngine);

        N2oInvocationProcessor invocationProcessor = new N2oInvocationProcessor(invocationFactory);

        N2oValidationModule validationModule = new N2oValidationModule(new ValidationProcessor(invocationProcessor));
        Map<String, DataProcessing> moduleMap = new HashMap<>();
        moduleMap.put("validationModule", validationModule);

        N2oOperationProcessor operationProcessor = new N2oOperationProcessor(invocationProcessor, new N2oOperationExceptionHandler());

        ApplicationContext context = Mockito.mock(ApplicationContext.class);
        Mockito.when(context.getBeansOfType(DataProcessing.class)).thenReturn(moduleMap);


        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/ui/controller/testObject.object.xml",
                "net/n2oapp/framework/ui/controller/testPage.page.xml",
                "net/n2oapp/framework/ui/controller/testQuery.query.xml"
        );
        pipeline.get(new PageContext("testPage"));
        N2oRouter router = new N2oRouter(builder.getEnvironment(), pipeline);
        ContextEngine contextEngine = Mockito.mock(ContextEngine.class);

        QueryProcessor queryProcessor = mock(QueryProcessor.class);
        Map<String, Object> map = new HashMap<>();
        AlertMessageBuilder messageBuilder = new AlertMessageBuilder(builder.getEnvironment().getMessageSource(), null);
        OperationController operationController = new OperationController(dataProcessingStack,
                operationProcessor, messageBuilder, builder.getEnvironment());
        QueryController queryController = new QueryController(dataProcessingStack, queryProcessor, null,
                messageBuilder, builder.getEnvironment());
        map.put("operationController", operationController);
        map.put("queryController", queryController);

        N2oControllerFactory factory = new N2oControllerFactory(map);
        factory.setEnvironment(builder.getEnvironment());

        DataController controller = new DataController(factory, builder.getEnvironment(), router);
        return controller;
    }
}
