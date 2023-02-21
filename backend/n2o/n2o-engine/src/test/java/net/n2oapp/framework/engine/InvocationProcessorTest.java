package net.n2oapp.framework.engine;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.util.TestEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование процессора выполнения действий, проверяется корректность работы маппинга входных и выходных параметров
 */
public class InvocationProcessorTest {

    private N2oInvocationProcessor invocationProcessor;
    private JavaDataProviderEngine javaDataProviderEngine;

    @Before
    public void setUp() throws Exception {
        N2oInvocationFactory actionInvocationFactory = mock(N2oInvocationFactory.class);
        javaDataProviderEngine = new JavaDataProviderEngine();
        when(actionInvocationFactory.produce(N2oJavaDataProvider.class)).thenReturn(javaDataProviderEngine);
        SqlInvocationEngine sqlInvocationEngine = new SqlInvocationEngine();
        when(actionInvocationFactory.produce(N2oSqlDataProvider.class)).thenReturn(sqlInvocationEngine);
        TestDataProviderEngine testDataProviderEngine = new TestDataProviderEngine();
        when(actionInvocationFactory.produce(N2oTestDataProvider.class)).thenReturn(testDataProviderEngine);
        ContextProcessor processor = mock(ContextProcessor.class);
        when(processor.resolve(anyMap())).thenAnswer((Answer<DataSet>) invocation -> (DataSet) invocation.getArguments()[0]);
        when(processor.resolve(anyString())).thenAnswer((Answer<String>) invocation -> (String) invocation.getArguments()[0]);
        when(processor.resolve(anyInt())).thenAnswer((Answer<Integer>) invocation -> (Integer) invocation.getArguments()[0]);
        when(processor.resolve(anyList())).thenAnswer((Answer<List>) invocation -> (List) invocation.getArguments()[0]);
        when(processor.resolve(anyBoolean())).thenAnswer((Answer<Boolean>) invocation -> (Boolean) invocation.getArguments()[0]);
        N2oEnvironment env = new N2oEnvironment();
        env.setContextProcessor(processor);
        invocationProcessor = new N2oInvocationProcessor(actionInvocationFactory);
        invocationProcessor.setEnvironment(env);
    }

    @Test
    public void testDefaultValue() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithOneArgument");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("entityTypeArgument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        entityTypeArgument.setType(Argument.Type.ENTITY);

        method.setArguments(new Argument[]{entityTypeArgument});

        ObjectReferenceField param = new ObjectReferenceField();
        param.setId("entity");
        param.setMapping("[0].innerObj");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField childParam = new ObjectSimpleField();
        childParam.setId("name");
        childParam.setMapping("valueStr");
        childParam.setDefaultValue("defaultValue");
        childParam.setNormalize("#this.toLowerCase()");
        param.setFields(new AbstractParameter[]{childParam});

        DataSet outerDataSet = new DataSet();
        outerDataSet.put("entity", new DataSet());

        DataSet resultDataSet = invocationProcessor.invoke(method, outerDataSet, Arrays.asList(param), null);

        assertThat(((DataSet) resultDataSet.get("entity")).get("name"), is("defaultValue"));
    }

    @Test
    public void testArgumentsInvocation() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("sum");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("entityTypeArgument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        entityTypeArgument.setType(Argument.Type.ENTITY);

        Argument primitiveTypeArgument = new Argument();
        primitiveTypeArgument.setName("primitiveTypeArgument");
        primitiveTypeArgument.setClassName("java.lang.Integer");
        primitiveTypeArgument.setType(Argument.Type.PRIMITIVE);
        primitiveTypeArgument.setDefaultValue("2");

        Argument noClassPrimitive = new Argument();
        noClassPrimitive.setName("noClassPrimitive");
        noClassPrimitive.setType(Argument.Type.PRIMITIVE);

        Argument classTypeArgument = new Argument();
        classTypeArgument.setName("classTypeArgument");
        classTypeArgument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        classTypeArgument.setType(Argument.Type.CLASS);

        method.setArguments(new Argument[]{entityTypeArgument, primitiveTypeArgument, noClassPrimitive, classTypeArgument});

        DataSet dataSet = new DataSet();
        dataSet.put("entity", 1);
        dataSet.put("primitive2", 100);
        dataSet.put("class", 7);
        dataSet.put("paramWithMappingCondition", null);

        List<AbstractParameter> inMapping = new ArrayList<>();
        ObjectSimpleField param1 = new ObjectSimpleField();
        param1.setId("entity");
        param1.setMapping("[0].testField");
        inMapping.add(param1);

        ObjectSimpleField param2 = new ObjectSimpleField();
        param2.setId("primitive");
        param2.setMapping("[1]");
        param2.setEnabled("primitive != null");
        inMapping.add(param2);

        ObjectSimpleField param3 = new ObjectSimpleField();
        param3.setId("primitive2");
        param3.setMapping("[2]");
        param3.setEnabled("primitive2 != null");
        inMapping.add(param3);

        ObjectSimpleField param4 = new ObjectSimpleField();
        param4.setId("class");
        param4.setMapping("[3].testField");
        inMapping.add(param4);

        ObjectSimpleField param5 = new ObjectSimpleField();
        param5.setId("paramWithMappingCondition");
        param5.setMapping("[4]");
        param5.setEnabled("paramWithMappingCondition != null");
        inMapping.add(param5);

        List<ObjectSimpleField> outMapping = new ArrayList<>();
        ObjectSimpleField outParam = new ObjectSimpleField();
        outParam.setId("sum");
        outParam.setMapping("#this");
        outMapping.add(outParam);

        DataSet resultDataSet = invocationProcessor.invoke(method, dataSet, inMapping, outMapping);
        assert resultDataSet.size() == 6;
        assert resultDataSet.get("sum").equals(110);
    }

    @Test
    public void testEntityMapping() {
        testDefaultValue();
        testNormalizing();
        testMappingCondition();
        fullMappingTest();
    }

    @Test
    public void testMappingCondition() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodReturnedEntity");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("argument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        entityTypeArgument.setType(Argument.Type.ENTITY);
        method.setArguments(new Argument[]{entityTypeArgument});

        DataSet dataSet = new DataSet();
        DataSet inDataSet = new DataSet();
        inDataSet.put("name", "testName");
        inDataSet.put("id", 123);
        dataSet.put("entity", inDataSet);

        List<AbstractParameter> inMapping = new ArrayList<>();
        ObjectReferenceField param = new ObjectReferenceField();
        param.setId("entity");
        param.setMapping("[0].innerObj");
        param.setEnabled("entity.id != null");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField inParam1 = new ObjectSimpleField();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        ObjectSimpleField inParam2 = new ObjectSimpleField();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        param.setFields(new AbstractParameter[]{inParam1, inParam2});
        inMapping.add(param);

        List<ObjectSimpleField> outMapping = new ArrayList<>();
        ObjectSimpleField outParam = new ObjectSimpleField();
        outParam.setId("result");
        outParam.setMapping("#this");
        outMapping.add(outParam);

        DataSet resultDataSet = invocationProcessor.invoke(method, dataSet, inMapping, outMapping);


        assertThat(((TestEntity) resultDataSet.get("result")).getInnerObj().getValueInt(), is(123));
        assertThat(((TestEntity) resultDataSet.get("result")).getInnerObj().getValueStr(), is("testName"));

        inDataSet.put("id", null);
        dataSet.put("entity", inDataSet);
        resultDataSet = invocationProcessor.invoke(method, dataSet, inMapping, outMapping);
        assertThat(((TestEntity) resultDataSet.get("result")).getInnerObj(), is(nullValue()));
    }

    @Test
    public void fullMappingTest() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodReturnedEntity");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("argument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        entityTypeArgument.setType(Argument.Type.ENTITY);
        method.setArguments(new Argument[]{entityTypeArgument});

        DataSet dataSet = new DataSet();
        DataSet inDataSet = new DataSet();
        inDataSet.put("name", "testName");
        inDataSet.put("id", 123);
        dataSet.put("entity", inDataSet);

        List<DataSet> list = new ArrayList<>();

        inDataSet = new DataSet();
        inDataSet.put("id", 1234);
        inDataSet.put("name", "testName1");
        list.add(inDataSet);

        inDataSet = new DataSet();
        inDataSet.put("name", "testName2");
        inDataSet.put("id", 12345);
        list.add(inDataSet);

        dataSet.put("entities", list);

        List<AbstractParameter> inMapping = new ArrayList<>();
        ObjectReferenceField param = new ObjectReferenceField();
        param.setId("entity");
        param.setMapping("[0].innerObj");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField inParam1 = new ObjectSimpleField();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        ObjectSimpleField inParam2 = new ObjectSimpleField();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        param.setFields(new AbstractParameter[]{inParam1, inParam2});
        inMapping.add(param);

        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setMapping("[0].innerObjList");
        listParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        inParam1 = new ObjectSimpleField();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        inParam1.setDefaultValue("testName1");
        inParam1.setNormalize("#this.toLowerCase()");
        inParam2 = new ObjectSimpleField();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        listParam.setFields(new AbstractParameter[]{inParam1, inParam2});
        inMapping.add(listParam);

        List<ObjectSimpleField> outMapping = new ArrayList<>();
        ObjectSimpleField outParam = new ObjectSimpleField();
        outParam.setId("testValue");
        outParam.setMapping("#this");
        outMapping.add(outParam);

        DataSet resultDataSet = invocationProcessor.invoke(method, dataSet, inMapping, outMapping);

        assertThat(resultDataSet.size(), is(3));
        TestEntity resultValue = (TestEntity) resultDataSet.get("testValue");
        assertThat(resultValue.getInnerObj().getValueInt(), is(123));
        assertThat(resultValue.getInnerObj().getValueStr(), is("testName"));
        assertThat(resultValue.getInnerObjList().get(0).getValueStr(), is("testname1"));
        assertThat(resultValue.getInnerObjList().get(1).getValueInt(), is(12345));
        assertThat(resultValue.getInnerObjList().get(1).getValueStr(), is("testname2"));
    }

    @Test
    public void testMapsInvocation() {
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        List<AbstractParameter> inMapping = new ArrayList<>();
        ObjectSimpleField param1 = new ObjectSimpleField();
        param1.setId("id");
        param1.setMapping("['id']");
        inMapping.add(param1);
        List<ObjectSimpleField> outMapping = new ArrayList<>();
        ObjectSimpleField outParam = new ObjectSimpleField();
        outParam.setId("id");
        outParam.setMapping("[0][0][0]");
        outMapping.add(outParam);
        N2oSqlDataProvider invocation = new N2oSqlDataProvider();
        invocation.setQuery("select 1");
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inMapping, outMapping);
        assert result.size() == 1;
        assert result.get("id").equals(1);
    }

    @Test
    public void testNormalizing() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodReturnedEntity");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        entityTypeArgument.setType(Argument.Type.ENTITY);

        method.setArguments(new Argument[]{entityTypeArgument});

        ObjectSimpleField childParam = new ObjectSimpleField();
        childParam.setId("name");
        childParam.setMapping("[0].valueStr");
        childParam.setNormalize("#this.toLowerCase()");

        DataSet innerDataSet = new DataSet();
        innerDataSet.put("name", "TESTSTRING");

        ObjectSimpleField outField = new ObjectSimpleField();
        outField.setId("entity");
        outField.setMapping("#this");
        Collection<ObjectSimpleField> outMapping = Arrays.asList(outField);

        DataSet resultDataSet = invocationProcessor.invoke(method, innerDataSet, Arrays.asList(childParam), outMapping);

        assertThat(((TestEntity) resultDataSet.get("entity")).getValueStr(), is("teststring"));
    }

    @Test
    public void testListNormalizing() {
        N2oTestDataProvider invocation = new N2oTestDataProvider();
        invocation.setOperation(N2oTestDataProvider.Operation.echo);

        //List
        ObjectListField listField = new ObjectListField();
        listField.setId("employees");
        listField.setNormalize("#this.?[['id'] != 1]");

        //Inner simple1
        ObjectSimpleField simpleListChildField1 = new ObjectSimpleField();
        simpleListChildField1.setId("id");
        simpleListChildField1.setNormalize("#this + 100");

        //Inner simple2
        ObjectSimpleField simpleListChildField2 = new ObjectSimpleField();
        simpleListChildField2.setId("name");
        simpleListChildField2.setNormalize("#this.toUpperCase()");

        //Inner simple3
        ObjectSimpleField simpleListChildField3 = new ObjectSimpleField();
        simpleListChildField3.setId("name2");
        simpleListChildField3.setNormalize("#data['name']");

        listField.setFields(new AbstractParameter[]{simpleListChildField1, simpleListChildField2, simpleListChildField3});

        //Out simple fields
        ObjectSimpleField simpleOutId = new ObjectSimpleField();
        simpleOutId.setId("id");
        simpleOutId.setMapping("['employees[0].id']");
        ObjectSimpleField simpleOutName = new ObjectSimpleField();
        simpleOutName.setId("name");
        simpleOutName.setMapping("['employees[0].name']");
        ObjectSimpleField simpleOutName2 = new ObjectSimpleField();
        simpleOutName2.setId("name2");
        simpleOutName2.setMapping("['employees[0].name2']");
        ObjectSimpleField simpleOutSize = new ObjectSimpleField();
        simpleOutSize.setId("processedListSize");
        simpleOutSize.setMapping("['employees'].size()");

        //DATA
        DataSet innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 1);
        innerDataSet1.put("name", "test1");
        DataSet innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 2);
        innerDataSet2.put("name", "test2");

        DataList dataList = new DataList();
        dataList.add(innerDataSet1);
        dataList.add(innerDataSet2);

        DataSet dataSet = new DataSet("employees", dataList);

        DataSet result = invocationProcessor.invoke(invocation, dataSet, singletonList(listField),
                Arrays.asList(simpleOutId, simpleOutName, simpleOutName2, simpleOutSize));
        assertThat(result.getInteger("id"), is(102));
        assertThat(result.getString("name"), is("TEST2"));
        assertThat(result.getString("name2"), is("test2"));
        assertThat(result.getInteger("processedListSize"), is(1));
    }

    @Test
    public void testAdvancedNestingWithMapInvocationProvider() {
        N2oTestDataProvider invocation = new N2oTestDataProvider();
        invocation.setOperation(N2oTestDataProvider.Operation.echo);

        // STRUCTURE
        //Reference
        ObjectReferenceField refParam = new ObjectReferenceField();
        refParam.setId("entity");
        refParam.setMapping("['refEntity']");

        //Simple
        ObjectSimpleField simpleParam = new ObjectSimpleField();
        simpleParam.setId("valueStr");

        //List
        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setMapping("['innerObjList']");
        ObjectSimpleField childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("['valueInt']");
        ObjectSimpleField childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("['valueStr']");
        //List Set
        ObjectSetField setParam = new ObjectSetField();
        setParam.setId("set");
        setParam.setMapping("['innerInnerObjSet']");
        ObjectSimpleField setChildParam = new ObjectSimpleField();
        setChildParam.setId("name");
        setChildParam.setMapping("['innerName']");
        setParam.setFields(new AbstractParameter[]{setChildParam});

        listParam.setFields(new AbstractParameter[]{childParam1, childParam2, setParam});
        refParam.setFields(new AbstractParameter[]{simpleParam, listParam});

        // DATASET
        DataSet innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 666);
        innerDataSet1.put("name", "testStr1");
        HashSet innerInnerDataSet = new HashSet();
        innerDataSet1.put("set", innerInnerDataSet);
        innerInnerDataSet.add(new DataSet("name", "code1"));
        innerInnerDataSet.add(new DataSet("name", "code2"));

        DataSet innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");
        innerInnerDataSet = new HashSet();
        innerDataSet2.put("set", innerInnerDataSet);
        innerInnerDataSet.add(new DataSet("name", "code3"));

        List list = Arrays.asList(innerDataSet1, innerDataSet2);

        DataSet refParamFields = new DataSet();
        refParamFields.put("entities", list);
        refParamFields.put("valueStr", "test");

        DataSet dataSet = new DataSet();
        dataSet.put("entity", refParamFields);


        List<AbstractParameter> inParameters = Arrays.asList(refParam);

        List<ObjectSimpleField> outParameters = new ArrayList<>();
        ObjectSimpleField entitiesField = new ObjectSimpleField();
        entitiesField.setId("outEntity");
        entitiesField.setMapping("['refEntity']");
        outParameters.add(entitiesField);

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        DataSet entity = (DataSet) result.get("outEntity");
        assertThat(entity.get("valueInt"), nullValue());
        assertThat(entity.get("innerObj"), nullValue());
        assertThat(entity.get("valueStr"), is("test"));
        DataList innerObjList = (DataList) entity.get("innerObjList");
        assertThat(innerObjList.size(), is(2));
        assertThat(((DataSet)innerObjList.get(0)).get("valueInt"), is(666));
        assertThat(((DataSet)innerObjList.get(0)).get("valueStr"), is("testStr1"));
        DataList innerInnerObjSet = (DataList) ((DataSet)innerObjList.get(0)).get("innerInnerObjSet");
        assertThat(innerInnerObjSet.size(), is(2));
        assertThat(innerInnerObjSet.contains(new DataSet("innerName", "code2")), is(true));
        assertThat(innerInnerObjSet.contains(new DataSet("innerName", "code1")), is(true));

        assertThat(((DataSet)innerObjList.get(1)).get("valueInt"), is(777));
        assertThat(((DataSet)innerObjList.get(1)).get("valueStr"), is("testStr2"));
        innerInnerObjSet = (DataList) ((DataSet) innerObjList.get(1)).get("innerInnerObjSet");
        assertThat(innerInnerObjSet.size(), is(1));
        assertThat(innerInnerObjSet.contains(new DataSet("innerName", "code3")), is(true));
    }

    @Test
    public void testMappingWithArgumentsInvocationProvider() {
        N2oJavaDataProvider invocation = new N2oJavaDataProvider();
        invocation.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        invocation.setMethod("methodWithTwoArguments");
        Argument argument1 = new Argument();
        argument1.setType(Argument.Type.PRIMITIVE);
        Argument argument2 = new Argument();
        argument2.setType(Argument.Type.PRIMITIVE);
        invocation.setArguments(new Argument[]{argument1, argument2});

        // STRUCTURE
        ObjectSimpleField firstArg = new ObjectSimpleField();
        firstArg.setId("firstArgument");
        ObjectSimpleField secondArg = new ObjectSimpleField();
        secondArg.setId("secondArgument");

        // DATASET
        DataSet dataSet = new DataSet();
        dataSet.put("firstArgument", "test");
        dataSet.put("secondArgument", 123);

        List<AbstractParameter> inParameters = Arrays.asList(firstArg, secondArg);

        ObjectSimpleField response = new ObjectSimpleField();
        response.setId("result");
        response.setMapping("#this");
        List<ObjectSimpleField> outParameters = Arrays.asList(response);

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        assertThat(result.get("result"), is("Invocation success. First argument: test, Second argument: 123"));


        // DATASET with null value
        dataSet = new DataSet();
        dataSet.put("firstArgument", "test");
        dataSet.put("secondArgument", null);

        // Result
        result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        assertThat(result.get("result"), is("Invocation success. First argument: test, Second argument: null"));

    }

    /**
     * Тестирование маппинга аргументов java провайдера с использованием name аргументов, а не через заданный порядок
     */
    @Test
    public void testNameMappingWithArgumentsInvocationProvider() {
        N2oJavaDataProvider invocation = new N2oJavaDataProvider();
        invocation.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        invocation.setMethod("methodWithThreeArguments");
        Argument argument1 = new Argument();
        argument1.setName("first");
        argument1.setType(Argument.Type.PRIMITIVE);
        Argument argument2 = new Argument();
        argument2.setName("second");
        argument2.setType(Argument.Type.PRIMITIVE);
        Argument argument3 = new Argument();
        argument3.setName("third");
        argument3.setType(Argument.Type.PRIMITIVE);
        invocation.setArguments(new Argument[]{argument1, argument2, argument3});

        // STRUCTURE
        ObjectSimpleField firstArg = new ObjectSimpleField();
        firstArg.setId("a");
        firstArg.setMapping("['first']");
        ObjectSimpleField secondArg = new ObjectSimpleField();
        secondArg.setId("b");
        secondArg.setMapping("[second]");
        ObjectSimpleField thirdArg = new ObjectSimpleField();
        thirdArg.setId("c");
        thirdArg.setMapping("['third']");
        List<AbstractParameter> inParameters = Arrays.asList(secondArg, firstArg, thirdArg);

        // DATASET
        DataSet dataSet = new DataSet();
        dataSet.put("c", true);
        dataSet.put("b", 123);
        dataSet.put("a", "test");

        ObjectSimpleField response = new ObjectSimpleField();
        response.setId("result");
        response.setMapping("#this");
        List<ObjectSimpleField> outParameters = Arrays.asList(response);

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        assertThat(result.get("result"), is("Invocation success. First argument: test, Second argument: 123, Third argument: true"));
    }

    @Test
    public void testAdvancedNestingWithArgumentsInvocationProvider() {
        N2oJavaDataProvider invocation = new N2oJavaDataProvider();
        invocation.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        invocation.setMethod("methodReturnedEntity");
        Argument argument = new Argument();
        argument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        argument.setType(Argument.Type.ENTITY);
        invocation.setArguments(new Argument[]{argument});

        // STRUCTURE
        //Simple
        ObjectSimpleField simpleParam = new ObjectSimpleField();
        simpleParam.setId("valueStr");
        simpleParam.setMapping("[0].valueStr");

        //List
        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setMapping("[0].innerObjList");
        listParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        ObjectSimpleField childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        //List Set
        ObjectSetField setParam = new ObjectSetField();
        setParam.setId("set");
        setParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity$InnerInnerEntity");
        setParam.setMapping("innerInnerObjSet");
        ObjectSimpleField setChildParam = new ObjectSimpleField();
        setChildParam.setId("name");
        setChildParam.setMapping("innerName");
        setParam.setFields(new AbstractParameter[]{setChildParam});

        listParam.setFields(new AbstractParameter[]{setParam, childParam2, childParam1});

        // DATASET
        DataSet innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 666);
        innerDataSet1.put("name", "testStr1");
        HashSet innerInnerDataSet = new HashSet();
        innerDataSet1.put("set", innerInnerDataSet);
        innerInnerDataSet.add(new DataSet("name", "code1"));
        innerInnerDataSet.add(new DataSet("name", "code2"));

        DataSet innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");
        innerInnerDataSet = new HashSet();
        innerDataSet2.put("set", innerInnerDataSet);
        innerInnerDataSet.add(new DataSet("name", "code3"));

        List list = Arrays.asList(innerDataSet1, innerDataSet2);

        DataSet dataSet = new DataSet();
        dataSet.put("entities", list);
        dataSet.put("valueStr", "test");

        List<AbstractParameter> inParameters = Arrays.asList(listParam, simpleParam);

        List<ObjectSimpleField> outParameters = new ArrayList<>();
        ObjectSimpleField entitiesField = new ObjectSimpleField();
        entitiesField.setId("outEntities");
        entitiesField.setMapping("innerObjList");
        outParameters.add(entitiesField);

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        assertThat(result.size(), is(3));
        assertThat(result.get("valueStr"), is("test"));
        List<TestEntity.InnerEntity> innerObjList = (List<TestEntity.InnerEntity>) result.get("outEntities");
        assertThat(innerObjList.size(), is(2));
        assertThat(innerObjList.get(0).getValueInt(), is(666));
        assertThat(innerObjList.get(0).getValueStr(), is("testStr1"));
        Set<TestEntity.InnerEntity.InnerInnerEntity> innerInnerObjSet = innerObjList.get(0).getInnerInnerObjSet();
        assertThat(innerInnerObjSet.size(), is(2));
        assertThat(innerInnerObjSet.contains(new TestEntity.InnerEntity.InnerInnerEntity("code1")), is(true));
        assertThat(innerInnerObjSet.contains(new TestEntity.InnerEntity.InnerInnerEntity("code2")), is(true));

        assertThat(innerObjList.get(1).getValueInt(), is(777));
        assertThat(innerObjList.get(1).getValueStr(), is("testStr2"));
        innerInnerObjSet = innerObjList.get(1).getInnerInnerObjSet();
        assertThat(innerInnerObjSet.size(), is(1));
        assertThat(innerInnerObjSet.contains(new TestEntity.InnerEntity.InnerInnerEntity("code3")), is(true));
    }

    @Test
    public void testResultMapping() {
        N2oTestDataProvider invocation = new N2oTestDataProvider();
        invocation.setResultMapping("['organization']");
        invocation.setOperation(N2oTestDataProvider.Operation.echo);

        //List
        ObjectReferenceField refField = new ObjectReferenceField();
        refField.setId("organization");

        //Inner simple1
        ObjectSimpleField childField1 = new ObjectSimpleField();
        childField1.setId("id");

        //Inner simple2
        ObjectSimpleField childField2 = new ObjectSimpleField();
        childField2.setId("name");

        refField.setFields(new AbstractParameter[]{childField1, childField2});

        //Out simple fields
        ObjectSimpleField simpleOutId = new ObjectSimpleField();
        simpleOutId.setId("myId");
        simpleOutId.setMapping("['id']");
        ObjectSimpleField simpleOutName = new ObjectSimpleField();
        simpleOutName.setId("myName");
        simpleOutName.setMapping("['name']");

        //DATA
        DataSet innerDataSet = new DataSet();
        innerDataSet.put("id", 1);
        innerDataSet.put("name", "test1");

        DataSet dataSet = new DataSet("organization", innerDataSet);

        DataSet result = invocationProcessor.invoke(invocation, dataSet, singletonList(refField),
                Arrays.asList(simpleOutId, simpleOutName));

        assertThat(result.getInteger("myId"), is(1));
        assertThat(result.getString("myName"), is("test1"));
    }


    public static class SqlInvocationEngine implements MapInvocationEngine<N2oSqlDataProvider> {

        @Override
        public Object invoke(N2oSqlDataProvider invocation, Map<String, Object> data) {
            return new Object[]{new Object[]{new Object[]{new Integer(1), "test"}}};
        }

        @Override
        public Class<N2oSqlDataProvider> getType() {
            return N2oSqlDataProvider.class;
        }
    }
}
