package net.n2oapp.framework.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
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

        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entity");
        param.setMapping("innerObj");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        N2oObject.Parameter childParam = new N2oObject.Parameter();
        childParam.setId("name");
        childParam.setMapping("valueStr");
        childParam.setDefaultValue("defaultValue");
        childParam.setNormalize("#this.toLowerCase()");
        param.setChildParams(new N2oObject.Parameter[]{childParam});

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

        List<InvocationParameter> inMapping = new ArrayList<>();
        InvocationParameter param1 = new InvocationParameter();
        param1.setId("entity");
        param1.setMapping("testField");
        inMapping.add(param1);

        InvocationParameter param2 = new InvocationParameter();
        param2.setId("primitive");
        param2.setMapping("[1]");
        param2.setEnabled("primitive != null");
        inMapping.add(param2);

        InvocationParameter param3 = new InvocationParameter();
        param3.setId("primitive2");
        param3.setMapping("[2]");
        param3.setEnabled("primitive2 != null");
        inMapping.add(param3);

        InvocationParameter param4 = new InvocationParameter();
        param4.setId("class");
        param4.setMapping("[3].testField");
        inMapping.add(param4);

        InvocationParameter param5 = new InvocationParameter();
        param5.setId("paramWithMappingCondition");
        param5.setMapping("[4]");
        param5.setEnabled("paramWithMappingCondition != null");
        inMapping.add(param5);

        List<InvocationParameter> outMapping = new ArrayList<>();
        InvocationParameter outParam = new InvocationParameter();
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

        List<InvocationParameter> inMapping = new ArrayList<>();
        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entity");
        param.setMapping("innerObj");
        param.setEnabled("entity.id != null");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        N2oObject.Parameter inParam1 = new N2oObject.Parameter();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        N2oObject.Parameter inParam2 = new N2oObject.Parameter();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        param.setChildParams(new N2oObject.Parameter[]{inParam1, inParam2});
        inMapping.add(param);

        List<InvocationParameter> outMapping = new ArrayList<>();
        InvocationParameter outParam = new InvocationParameter();
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

        List<InvocationParameter> inMapping = new ArrayList<>();
        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entity");
        param.setMapping("innerObj");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        N2oObject.Parameter inParam1 = new N2oObject.Parameter();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        N2oObject.Parameter inParam2 = new N2oObject.Parameter();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        param.setChildParams(new N2oObject.Parameter[]{inParam1, inParam2});
        inMapping.add(param);

        N2oObject.Parameter listParam = new N2oObject.Parameter();
        listParam.setId("entities");
        listParam.setMapping("innerObjList");
        listParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        listParam.setPluralityType(PluralityType.list);
        inParam1 = new N2oObject.Parameter();
        inParam1.setId("name");
        inParam1.setMapping("valueStr");
        inParam1.setDefaultValue("testName1");
        inParam1.setNormalize("#this.toLowerCase()");
        inParam2 = new N2oObject.Parameter();
        inParam2.setId("id");
        inParam2.setMapping("valueInt");
        listParam.setChildParams(new N2oObject.Parameter[]{inParam1, inParam2});
        inMapping.add(listParam);

        List<InvocationParameter> outMapping = new ArrayList<>();
        InvocationParameter outParam = new InvocationParameter();
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
        List<InvocationParameter> inMapping = new ArrayList<>();
        InvocationParameter param1 = new InvocationParameter();
        param1.setId("id");
        param1.setMapping("['id']");
        inMapping.add(param1);
        List<InvocationParameter> outMapping = new ArrayList<>();
        InvocationParameter outParam = new InvocationParameter();
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

        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entity");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity");
        N2oObject.Parameter childParam = new N2oObject.Parameter();
        childParam.setId("name");
        childParam.setMapping("valueStr");
        childParam.setNormalize("#this.toLowerCase()");
        param.setChildParams(new N2oObject.Parameter[]{childParam});

        DataSet innerDataSet = new DataSet();
        innerDataSet.put("name", "TESTSTRING");

        DataSet outerDataSet = new DataSet();
        outerDataSet.put("entity", innerDataSet);

        DataSet resultDataSet = invocationProcessor.invoke(method, outerDataSet, Arrays.asList(param), null);

        assertThat(((TestEntity) resultDataSet.get("entity")).getValueStr(), is("teststring"));
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
