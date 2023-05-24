package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.N2oAlertMessagesConstructor;
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
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.N2oOperationExceptionHandler;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.ui.controller.action.OperationController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;


import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

public class SpellExceptionTest extends DataControllerTestBase{

    private ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> createPipeline() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/ui/controller/spellException/testObject.object.xml",
                "net/n2oapp/framework/ui/controller/spellException/testQuery.query.xml",
                "net/n2oapp/framework/ui/controller/spellException/testPage.page.xml");
        pipeline.get(new PageContext("testPage"));
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

        N2oValidationModule validationModule = new N2oValidationModule(new ValidationProcessor(invocationProcessor),
                new AlertMessageBuilder(builder.getEnvironment().getMessageSource(), null));
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
        N2oAlertMessagesConstructor messagesConstructor = new N2oAlertMessagesConstructor(messageBuilder);
        OperationController operationController = new OperationController(dataProcessingStack, operationProcessor,
                messageBuilder, builder.getEnvironment(), messagesConstructor);
        map.put("operationController", operationController);

        N2oControllerFactory factory = new N2oControllerFactory(map);
        factory.setEnvironment(builder.getEnvironment());

        DataController controller = new DataController(factory, builder.getEnvironment(), router);
        controller.setMessageBuilder(messageBuilder);
        return controller.setData(path, params , null, body, new UserContext(contextEngine));
    }

    @Test
    public void testObject() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = createPipeline();
        Map<String, String[]> params = new HashMap<>();
        DataSet body = new DataSet();
        N2oSpelException n2oSpelException = assertThrows(N2oSpelException.class, () -> testOperation("/page/create1", pipeline, params, body));
        assertThat(n2oSpelException.getMessage(), is("Spel expression conversion error with #this.toUpperCase( of field 'field1' in operation 'create1' from metadata testObject.object.xml. Cause: Expression [#this.toUpperCase(] @17: EL1051E: Unexpectedly ran out of arguments"));


    }

}
