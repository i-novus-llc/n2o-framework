package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.*;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.engine.data.*;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.engine.validation.N2oValidationModule;
import net.n2oapp.framework.engine.validation.engine.ValidationProcessor;
import net.n2oapp.framework.ui.controller.query.GetController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;


import java.util.*;

import static org.mockito.Mockito.mock;

public class SpellExceptionTest extends DataControllerTestBase{


    protected N2oApplicationBuilder builder;
    private TestSetController testSetController;

    private GetController getController;

    @BeforeEach
    public void setUp() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap());
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("n2o_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        environment.setMessageSource(new MessageSourceAccessor(messageSource));
        builder = new N2oApplicationBuilder(environment);
        configure(builder);
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
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

        QueryProcessor queryProcessor = new N2oQueryProcessor(invocationFactory, new N2oQueryExceptionHandler());
        AlertMessageBuilder messageBuilder = new AlertMessageBuilder(builder.getEnvironment().getMessageSource(), null);
        N2oAlertMessagesConstructor messagesConstructor = new N2oAlertMessagesConstructor(messageBuilder);

        testSetController = new TestSetController(dataProcessingStack, operationProcessor, builder.getEnvironment());
        getController = new QueryController(dataProcessingStack, queryProcessor, null,
                messageBuilder, builder.getEnvironment(), messagesConstructor);

    }

    private void configure(N2oApplicationBuilder builder) {
        builder.packs(new N2oSourceTypesPack(),
                new N2oDataProvidersPack(),
                new N2oObjectsPack(), new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(),
                new N2oOperationsPack(), new N2oQueriesPack());
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }


    @Test
    public void testNormalizeInParameter() {
        ActionRequestInfo actionRequestInfo = new ActionRequestInfo();
        CompiledObject object = new CompiledObject();
        object.setId("testObject");
        CompiledObject.Operation operation = new CompiledObject.Operation();
        operation.setId("test1");
        actionRequestInfo.setObject(object);
        Map<String, AbstractParameter> inParametersMap = new HashMap<>();
        ObjectSimpleField field = new ObjectSimpleField();
        field.setId("name");
        field.setNormalize("#this.toUpperCase(");
        inParametersMap.put("testId", field);

        Map<String, ObjectSimpleField> outParametersMap =  Collections.emptyMap();

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    public void testMappingInParameter() {
        ActionRequestInfo actionRequestInfo = new ActionRequestInfo();
        CompiledObject object = new CompiledObject();
        object.setId("testObject");
        CompiledObject.Operation operation = new CompiledObject.Operation();
        operation.setId("test1");
        actionRequestInfo.setObject(object);
        Map<String, AbstractParameter> inParametersMap = new HashMap<>();
        ObjectSimpleField field = new ObjectSimpleField();
        field.setId("name");
        field.setMapping("['name'].toUpperCase(");
        inParametersMap.put("testId", field);

        Map<String, ObjectSimpleField> outParametersMap =  Collections.emptyMap();

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        operation.setInvocation(new N2oTestDataProvider());
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['name'].toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertEquals(exception.getMessage(), exceptionMessage);
//        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    public void testNormalizeOutParameter() {
        ActionRequestInfo actionRequestInfo = new ActionRequestInfo();
        CompiledObject object = new CompiledObject();
        object.setId("testObject");
        CompiledObject.Operation operation = new CompiledObject.Operation();
        operation.setId("test1");
        actionRequestInfo.setObject(object);
        Map<String, AbstractParameter> inParametersMap = new HashMap<>();
        ObjectSimpleField field = new ObjectSimpleField();
        field.setId("name");
        field.setDefaultValue("test");
        inParametersMap.put("testId", field);

        Map<String, ObjectSimpleField> outParametersMap = new HashMap<>();
        field = new ObjectSimpleField();
        field.setId("name");
        field.setNormalize("#this.toUpperCase(");
        outParametersMap.put("testId", field);

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        N2oTestDataProvider testDataProvider = new N2oTestDataProvider();
        testDataProvider.setFile("net/n2oapp/framework/ui/controller/testData.json");
        testDataProvider.setOperation(N2oTestDataProvider.Operation.create);
        operation.setInvocation(testDataProvider);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }
    @Test
    public void testNormalizeQuery() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
//        Map<String, AbstractField> fieldsMap = new HashMap<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setNormalize("#this.toUpperCase(");
//        fieldsMap.put("name", field);
        displayFields.add(field);
//        query.setFieldsMap(fieldsMap);
        query.setDisplayFields(displayFields);

        N2oTestDataProvider testDataProvider = new N2oTestDataProvider();
        testDataProvider.setFile("net/n2oapp/framework/ui/controller/testData.json");

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.Type.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
    }





}
