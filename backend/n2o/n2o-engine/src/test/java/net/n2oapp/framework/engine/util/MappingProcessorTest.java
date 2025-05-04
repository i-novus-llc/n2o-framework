package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование преобразования данных
 */
class MappingProcessorTest {

    @Test
    void testInMap() {
        TestEntity result = new TestEntity();
        TestEntity.InnerEntity innerEntity = new TestEntity.InnerEntity();
        result.setInnerObj(innerEntity);
        MappingProcessor.inMap(result, "valueStr", "valueStr", "object");
        MappingProcessor.inMap(result, "valueInt", "valueInt", 55);
        MappingProcessor.inMap(result, "innerObj.valueStr", "innerObj.valueStr", "inner");
        MappingProcessor.inMap(result, "innerObj.valueInt", "innerObj.valueInt", 66);
        assertEquals("inner", result.getInnerObj().getValueStr());
        assertEquals(66, result.getInnerObj().getValueInt());
        assertEquals("object", result.getValueStr());
        assertEquals(55, result.getValueInt());
    }

    @Test
    void testOutMap() {
        TestEntity test = new TestEntity();
        test.setValueStr("string");
        String result = MappingProcessor.outMap(test, "valueStr", String.class);
        assertEquals("string", result);

        ContextProcessor contextProcessor = Mockito.mock(ContextProcessor.class);
        Mockito.when(contextProcessor.resolve(11)).thenReturn(11);
        DataSet res = new DataSet();
        MappingProcessor.outMap(res, test, "fieldId", "valueStr", null, contextProcessor);
        MappingProcessor.outMap(res, test, "fieldId2", "valueInt", 11, contextProcessor);
        assertEquals("string", res.get("fieldId"));
        assertEquals(11, res.get("fieldId2"));
    }

    @Test
    void testMapParameters() {
        testEntityMapping();
        testEntityCollectionMapping();
    }

    private void testEntityMapping() {
        ObjectReferenceField param = new ObjectReferenceField();
        param.setId("entity");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        ObjectSimpleField childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        param.setFields(new AbstractParameter[]{childParam1, childParam2});

        DataSet innerDataSet = new DataSet();
        innerDataSet.put("id", 123);
        innerDataSet.put("name", "testStr");

        DataSet outerDataSet = new DataSet();
        outerDataSet.put("entity", innerDataSet);

        MappingProcessor.mapParameter(param, outerDataSet);

        assertTrue(outerDataSet.get("entity") instanceof TestEntity.InnerEntity);
        TestEntity.InnerEntity innerEntity = (TestEntity.InnerEntity) outerDataSet.get("entity");
        assertEquals(123, innerEntity.getValueInt());
        assertEquals("testStr", innerEntity.getValueStr());
    }

    private void testEntityCollectionMapping() {
        //List
        ObjectListField listParam = new ObjectListField();
        listParam.setId("entities");
        listParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        ObjectSimpleField childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        ObjectSimpleField childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        listParam.setFields(new AbstractParameter[]{childParam1, childParam2});

        DataSet innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 123);
        innerDataSet1.put("name", "testStr1");

        DataSet innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");

        List list = new ArrayList();
        list.add(innerDataSet1);
        list.add(innerDataSet2);

        DataSet outerDataSetWithList = new DataSet();
        outerDataSetWithList.put("entities", list);

        MappingProcessor.mapParameter(listParam, outerDataSetWithList);

        assertTrue(outerDataSetWithList.get("entities") instanceof List);
        List<TestEntity.InnerEntity> entities = (List<TestEntity.InnerEntity>) outerDataSetWithList.get("entities");
        assertEquals(123, entities.get(0).getValueInt());
        assertEquals("testStr1", entities.get(0).getValueStr());
        assertEquals(777, entities.get(1).getValueInt());
        assertEquals("testStr2", entities.get(1).getValueStr());


        //Set
        ObjectSetField setParam = new ObjectSetField();
        setParam.setId("entities");
        setParam.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        childParam1 = new ObjectSimpleField();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        childParam2 = new ObjectSimpleField();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        setParam.setFields(new AbstractParameter[]{childParam1, childParam2});

        innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 123);
        innerDataSet1.put("name", "testStr1");

        innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");

        Set set = new HashSet();
        set.add(innerDataSet1);
        set.add(innerDataSet2);

        DataSet outerDataSetWithSet = new DataSet();
        outerDataSetWithSet.put("entities", set);

        MappingProcessor.mapParameter(setParam, outerDataSetWithSet);

        assertTrue(outerDataSetWithSet.get("entities") instanceof Set);
        assertTrue(((Set) outerDataSetWithSet.get("entities")).containsAll((List) outerDataSetWithList.get("entities")));
    }

    @Test
    void testExtractMapping() {
        Map<String, AbstractParameter> parameters = new LinkedHashMap<>();
        ObjectSimpleField param = new ObjectSimpleField();
        param.setId("a");
        param.setMapping("x");
        parameters.put("a", param);
        Map<String, FieldMapping> mapping = MappingProcessor.extractFieldMapping(parameters.values());
        assertThat(mapping.get("a").getMapping(), is("x"));

        parameters = new LinkedHashMap<>();
        param = new ObjectSimpleField();
        param.setId("a");
        parameters.put("a", param);
        mapping = MappingProcessor.extractFieldMapping(parameters.values());
        assertThat(mapping.containsKey("a"), is(true));
        assertThat(mapping.get("a").getMapping(), is(nullValue()));
    }

    @Test
    void testResolveCondition() {
        Map<String, Object> data = Map.of("request", Map.of("status", "CREATED"));
        String falseCondition = "request.status == 'PROCESSING'";
        String trueCondition = "request.status == 'CREATED'";
        String errorCondition = "request.status == CREATED";

        assertThat(MappingProcessor.resolveCondition(falseCondition, data), is(false));
        assertThat(MappingProcessor.resolveCondition(trueCondition, data), is(true));
        assertThrows(N2oSpelException.class, () -> MappingProcessor.resolveCondition(errorCondition, data));
    }

    @Test
    void normalizeValue() {
        ExpressionParser parser = new SpelExpressionParser();
        BeanFactory beanFactory = mock(BeanFactory.class);
        when(beanFactory.getBean("myBean")).thenReturn(new MappingProcessorTest.MyBean());
        String obj = "test";
        DataSet data = new DataSet();
        data.put("name", "John");
        assertThat(MappingProcessor.normalizeValue(obj, "#this", data, parser, beanFactory), is("test"));
        assertThat(MappingProcessor.normalizeValue(obj, "#data['name']", data, parser, beanFactory), is("John"));
        assertThat(MappingProcessor.normalizeValue(obj, "#parent['name']", null, data, parser, beanFactory), is("John"));
        assertThat(MappingProcessor.normalizeValue(obj, "@myBean.call()", data, parser, beanFactory), is("Doe"));

        assertThrows(
                N2oSpelException.class,
                () -> MappingProcessor.normalizeValue(obj, "#this + '100", data, parser, beanFactory)
        );
    }

    @Test
    void testException() {
        ContextProcessor contextProcessor = Mockito.mock(ContextProcessor.class);

        N2oSpelException n2oSpelException = assertThrows(
                N2oSpelException.class,
                () -> MappingProcessor.outMap(new DataSet(), new DataSet(), "fieldId", "['id'] ? 0 :", null, contextProcessor)
        );
        assertThat(n2oSpelException.getCause(), instanceOf(IllegalArgumentException.class));
        assertThat(n2oSpelException.getFieldId(), is("fieldId"));
        assertThat(n2oSpelException.getMapping(), is("['id'] ? 0 :"));
    }

    static class MyBean {
        public String call() {
            return "Doe";
        }
    }
}
