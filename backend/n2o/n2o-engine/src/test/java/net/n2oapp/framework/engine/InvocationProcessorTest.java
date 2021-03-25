package net.n2oapp.framework.engine;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование процессора выполнения действий, проверяется корректность работы маппинга входных и выходных параметров
 */
public class InvocationProcessorTest {

    private N2oInvocationProcessor invocationProcessor;

    @Before
    public void setUp() throws Exception {
        N2oInvocationFactory actionInvocationFactory = mock(N2oInvocationFactory.class);
        when(actionInvocationFactory.produce(N2oJavaDataProvider.class)).thenReturn(new JavaDataProviderEngine());
        SqlInvocationEngine sqlInvocationEngine = new SqlInvocationEngine();
        when(actionInvocationFactory.produce(N2oSqlDataProvider.class)).thenReturn(sqlInvocationEngine);
        TestDataProviderEngine testDataProviderEngine = new TestDataProviderEngine();
        when(actionInvocationFactory.produce(N2oTestDataProvider.class)).thenReturn(testDataProviderEngine);
        ContextProcessor processor = mock(ContextProcessor.class);
        when(processor.resolve(anyMap())).thenAnswer((Answer<DataSet>) invocation -> (DataSet) invocation.getArguments()[0]);
        when(processor.resolve(anyString())).thenAnswer((Answer<String>) invocation -> (String) invocation.getArguments()[0]);
        when(processor.resolve(anyInt())).thenAnswer((Answer<Integer>) invocation -> (Integer) invocation.getArguments()[0]);
        when(processor.resolve(anyList())).thenAnswer((Answer<List>) invocation -> (List) invocation.getArguments()[0]);
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
        param.setMapping("innerObj");
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

        assertThat(((TestEntity.InnerEntity) resultDataSet.get("entity")).getValueStr(), is("defaultvalue"));
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
        param1.setMapping("testField");
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
        param.setMapping("innerObj");
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
        param.setMapping("innerObj");
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
        listParam.setMapping("innerObjList");
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
        assertThat(((TestEntity.InnerEntity) resultDataSet.get("entity")).getValueInt(), is(123));
        assertThat(((TestEntity.InnerEntity) resultDataSet.get("entity")).getValueStr(), is("testName"));
        assertThat(((List<TestEntity.InnerEntity>) resultDataSet.get("entities")).get(0).getValueStr(), is("testname1"));
        assertThat(((List<TestEntity.InnerEntity>) resultDataSet.get("entities")).get(1).getValueInt(), is(12345));
        assertThat(((List<TestEntity.InnerEntity>) resultDataSet.get("entities")).get(1).getValueStr(), is("testname2"));
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
        method.setMethod("methodWithOneArgument");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("entityTypeArgument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.util.TestEntity");
        entityTypeArgument.setType(Argument.Type.ENTITY);

        method.setArguments(new Argument[]{entityTypeArgument});

        ObjectReferenceField param = new ObjectReferenceField();
        param.setId("entity");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity");
        ObjectSimpleField childParam = new ObjectSimpleField();
        childParam.setId("name");
        childParam.setMapping("valueStr");
        childParam.setNormalize("#this.toLowerCase()");
        param.setFields(new AbstractParameter[]{childParam});

        DataSet innerDataSet = new DataSet();
        innerDataSet.put("name", "TESTSTRING");

        DataSet outerDataSet = new DataSet();
        outerDataSet.put("entity", innerDataSet);

        DataSet resultDataSet = invocationProcessor.invoke(method, outerDataSet, Arrays.asList(param), null);

        assertThat(((TestEntity) resultDataSet.get("entity")).getValueStr(), is("teststring"));
    }

    @Test
    public void testAdvancedNestingWithMapInvocationProvider() {
        N2oTestDataProvider invocation = new N2oTestDataProvider();
        invocation.setOperation(N2oTestDataProvider.Operation.echo);

        // STRUCTURE
        //Reference
        ObjectReferenceField refParam = new ObjectReferenceField();
        refParam.setId("entity");
        refParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity");
        refParam.setMapping("['refEntity']");

        //Simple
        ObjectSimpleField simpleParam = new ObjectSimpleField();
        simpleParam.setId("valueStr");

        //List
        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setMapping("['innerObjList']");
        listParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("['valueInt']");
        ObjectSimpleField childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("['valueStr']");
        //List Set
        ObjectSetField setParam = new ObjectSetField();
        setParam.setId("set");
        setParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity$InnerInnerEntity");
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

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        TestEntity entity = (TestEntity) result.get("entity");
        assertThat(entity.getValueInt(), nullValue());
        assertThat(entity.getInnerObj(), nullValue());
        assertThat(entity.getValueStr(), is("test"));
        List<TestEntity.InnerEntity> innerObjList = entity.getInnerObjList();
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

        //List
        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setMapping("innerObjList");
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

        listParam.setFields(new AbstractParameter[]{childParam1, childParam2, setParam});

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

        List<AbstractParameter> inParameters = Arrays.asList(simpleParam, listParam);

        List<ObjectSimpleField> outParameters = new ArrayList<>();

        // Result
        DataSet result = invocationProcessor.invoke(invocation, dataSet, inParameters, outParameters);
        assertThat(result.size(), is(2));
        assertThat(result.get("valueStr"), is("test"));
        List<TestEntity.InnerEntity> innerObjList = (List<TestEntity.InnerEntity>) result.get("entities");
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
