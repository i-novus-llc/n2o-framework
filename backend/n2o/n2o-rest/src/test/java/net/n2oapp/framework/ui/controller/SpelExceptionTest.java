package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
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
import net.n2oapp.framework.api.user.UserContext;
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

class SpelExceptionTest extends DataControllerTestBase{

    private TestSetController testSetController;

    private QueryController getController;

    private N2oTestDataProvider testDataProvider;

    @BeforeEach
    public void setUp() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap(environment));
        environment.setContextProcessor(new ContextProcessor(new UserContext(Mockito.mock(ContextEngine.class))));
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("n2o_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        environment.setMessageSource(new MessageSourceAccessor(messageSource));
        N2oApplicationBuilder builder = new N2oApplicationBuilder(environment);
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

        N2oQueryProcessor queryProcessor = new N2oQueryProcessor(invocationFactory, new N2oQueryExceptionHandler());
        queryProcessor.setEnvironment(builder.getEnvironment());
        AlertMessageBuilder messageBuilder = new AlertMessageBuilder(builder.getEnvironment().getMessageSource(), null);
        N2oAlertMessagesConstructor messagesConstructor = new N2oAlertMessagesConstructor(messageBuilder);

        testSetController = new TestSetController(dataProcessingStack, operationProcessor);
        getController = new QueryController(dataProcessingStack, queryProcessor, null,
                messageBuilder, messagesConstructor);

        testDataProvider = new N2oTestDataProvider();
        testDataProvider.setFile("net/n2oapp/framework/ui/controller/testData.json");

    }

    private void configure(N2oApplicationBuilder builder) {
        builder.packs(new N2oDataProvidersPack(),
                new N2oObjectsPack(), new N2oOperationsPack(), new N2oQueriesPack());
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }


    @Test
    void testNormalizeInParameter() {
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

        Map<String, AbstractParameter> outParametersMap =  Collections.emptyMap();

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testMappingInParameter() {
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

        Map<String, AbstractParameter> outParametersMap =  Collections.emptyMap();

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        operation.setInvocation(new N2oTestDataProvider());
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['name'].toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testNormalizeOutParameter() {
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

        Map<String, AbstractParameter> outParametersMap = new HashMap<>();
        field = new ObjectSimpleField();
        field.setId("name");
        field.setNormalize("#this.toUpperCase(");
        outParametersMap.put("testId", field);

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        N2oTestDataProvider testDataProvider = new N2oTestDataProvider();
        testDataProvider.setFile("net/n2oapp/framework/ui/controller/testData.json");
        testDataProvider.setOperation(N2oTestDataProvider.OperationEnum.create);
        operation.setInvocation(testDataProvider);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }
    @Test
    void testMappingOutParameter() {
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

        Map<String, AbstractParameter> outParametersMap = new HashMap<>();
        field = new ObjectSimpleField();
        field.setId("name");
        field.setMapping("['name'.toUpperCase(");
        outParametersMap.put("testId", field);

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        testDataProvider.setOperation(N2oTestDataProvider.OperationEnum.create);
        operation.setInvocation(testDataProvider);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['name'.toUpperCase( of field 'name' in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void  testResultMappingObject() {
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

        Map<String, AbstractParameter> outParametersMap = new HashMap<>();
        field = new ObjectSimpleField();
        field.setId("name");
        outParametersMap.put("testId", field);

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        testDataProvider.setOperation(N2oTestDataProvider.OperationEnum.create);
        testDataProvider.setResultMapping("['test'].toUpperCase(");
        operation.setInvocation(testDataProvider);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['test'].toUpperCase( in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void  testResultNormalizeObject() {
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

        Map<String, AbstractParameter> outParametersMap = new HashMap<>();
        field = new ObjectSimpleField();
        field.setId("name");
        outParametersMap.put("testId", field);

        operation.setInParametersMap(inParametersMap);
        operation.setOutParametersMap(outParametersMap);
        testDataProvider.setOperation(N2oTestDataProvider.OperationEnum.create);
        testDataProvider.setResultNormalize("['test'].toUpperCase(");
        operation.setInvocation(testDataProvider);
        actionRequestInfo.setOperation(operation);
        actionRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> testSetController.handleActionRequest(actionRequestInfo, new ActionResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['test'].toUpperCase( in operation 'test1' from metadata testObject.object.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testNormalizeQueryField() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setNormalize("#this.toUpperCase(");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with null of field 'name' from metadata testQuery.query.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testMappingQueryField() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setMapping("#this.toUpperCase(");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( of field 'name' from metadata testQuery.query.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testResultMappingQuery() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setDefaultValue("test");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        Map<String, QuerySimpleField> simpleFieldsMap = new HashMap<>();
        simpleFieldsMap.put("name", field);
        query.setSimpleFieldsMap(simpleFieldsMap);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        list.setResultMapping("['test'].toUpperCase(");
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['test'].toUpperCase( from metadata testQuery.query.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testResultNormalizeQuery() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setDefaultValue("test");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        list.setResultNormalize("['test'].toUpperCase(");
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['test'].toUpperCase( from metadata testQuery.query.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testFiltersNormalize() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setDefaultValue("test");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        Map<String, QuerySimpleField> simpleFieldsMap = new HashMap<>();
        simpleFieldsMap.put("name", field);
        query.setSimpleFieldsMap(simpleFieldsMap);

        N2oQuery.Filter filter = new N2oQuery.Filter("name", FilterTypeEnum.eq);
        filter.setFieldId("name");
        filter.setNormalize("#this.toUpperCase(");
        filter.setDefaultValue("test");
        Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> filtersMap = new LinkedHashMap<>();
        filtersMap.put("name", Map.of(FilterTypeEnum.eq, filter));
        query.setFiltersMap(filtersMap);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria("name", "test", 1);

        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with #this.toUpperCase( from metadata testQuery.query.xml.";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }

    @Test
    void testFiltersMapping() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        CompiledQuery query = new CompiledQuery();
        query.setId("testQuery");

        List<AbstractField> displayFields = new ArrayList<>();
        QuerySimpleField field = new QuerySimpleField();
        field.setId("name");
        field.setDefaultValue("test");
        displayFields.add(field);
        query.setDisplayFields(displayFields);

        Map<String, QuerySimpleField> simpleFieldsMap = new HashMap<>();
        simpleFieldsMap.put("name", field);
        query.setSimpleFieldsMap(simpleFieldsMap);

        N2oQuery.Filter filter = new N2oQuery.Filter("name", FilterTypeEnum.eq);
        filter.setFieldId("name");
        filter.setMapping("['test'].toUpperCase(");
        filter.setDefaultValue("test");
        Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> filtersMap = new LinkedHashMap<>();
        filtersMap.put("name", Map.of(FilterTypeEnum.eq, filter));
        query.setFiltersMap(filtersMap);

        N2oQuery.Selection list = new N2oQuery.Selection(N2oQuery.Selection.TypeEnum.list, testDataProvider);
        N2oQuery.Selection[] lists = new N2oQuery.Selection[1];
        lists[0] = list;
        query.setLists(lists);
        N2oPreparedCriteria criteria = new N2oPreparedCriteria("name", "test", 1);

        queryRequestInfo.setCriteria(criteria);
        queryRequestInfo.setQuery(query);
        queryRequestInfo.setSize(1);
        queryRequestInfo.setData(new DataSet());
        N2oSpelException exception = Assertions.assertThrows(N2oSpelException.class, () -> getController.executeQuery(queryRequestInfo, new QueryResponseInfo()));
        String exceptionMessage = "Spel expression conversion error with ['test'].toUpperCase( of field 'name' from metadata testQuery.query.xml. ";
        Assertions.assertTrue(exception.getMessage().startsWith(exceptionMessage));
    }
}
