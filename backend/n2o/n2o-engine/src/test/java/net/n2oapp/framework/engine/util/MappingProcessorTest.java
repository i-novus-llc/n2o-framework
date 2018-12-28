package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;
import org.junit.Test;

import java.util.*;

/**
 * Тестирование преобразования данных
 */
public class MappingProcessorTest {

    @Test
    public void testInMap() {
        TestEntity result = new TestEntity();
        TestEntity.InnerEntity innerEntity = new TestEntity.InnerEntity();
        result.setInnerObj(innerEntity);
        MappingProcessor.inMap(result, "valueStr", "object");
        MappingProcessor.inMap(result, "valueInt", 55);
        MappingProcessor.inMap(result, "innerObj.valueStr", "inner");
        MappingProcessor.inMap(result, "innerObj.valueInt", 66);
        assert result.getInnerObj().getValueStr().equals("inner");
        assert result.getInnerObj().getValueInt().equals(66);
        assert result.getValueStr().equals("object");
        assert result.getValueInt().equals(55);
    }

    @Test
    public void testOutMap() {
        TestEntity test = new TestEntity();
        test.setValueStr("string");
        String result = MappingProcessor.outMap(test, "valueStr", String.class);
        assert result.equals("string");

        DataSet res = new DataSet();
        MappingProcessor.outMap(res, test, "fieldId", "valueStr", null);
        MappingProcessor.outMap(res, test, "fieldId2", "valueInt", 11);
        assert res.get("fieldId").equals("string");
        assert res.get("fieldId2").equals(11);
    }

    @Test
    public void testMap() {
        DataSet inDataSet = new DataSet();
        inDataSet.put("valueStr", "string");
        inDataSet.put("valueInt", 11);
        inDataSet.put("innerObjValueStr", "inner");
        inDataSet.put("innerObjValueInt", 14);
        Map<String, String> mapping = new HashMap<>();
        mapping.put("valueStr", "[0].valueStr");
        mapping.put("valueInt", "[0].valueInt");
        mapping.put("innerObjValueStr", "[0].innerObj.valueStr");
        mapping.put("innerObjValueInt", "[0].innerObj.valueInt");
        Object[] res = MappingProcessor.map(inDataSet, mapping, Arrays.asList("net.n2oapp.framework.engine.util.TestEntity"));
        TestEntity result = (TestEntity) res[0];
        assert result.getInnerObj().getValueStr().equals("inner");
        assert result.getInnerObj().getValueInt().equals(14);
        assert result.getValueStr().equals("string");
        assert result.getValueInt().equals(11);
    }

    @Test
    public void testMapParameters() {
        testEntityMapping();
        testEntityCollectionMapping();
    }

    private void testEntityMapping() {
        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entity");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        N2oObject.Parameter childParam1 = new N2oObject.Parameter();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        N2oObject.Parameter childParam2 = new N2oObject.Parameter();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        param.setChildParams(new N2oObject.Parameter[]{childParam1, childParam2});

        DataSet innerDataSet = new DataSet();
        innerDataSet.put("id", 666);
        innerDataSet.put("name", "testStr");

        DataSet outerDataSet = new DataSet();
        outerDataSet.put("entity", innerDataSet);

        MappingProcessor.mapParameter(param, outerDataSet);

        assert outerDataSet.get("entity") instanceof TestEntity.InnerEntity;
        assert ((TestEntity.InnerEntity) outerDataSet.get("entity")).getValueInt().equals(666);
        assert ((TestEntity.InnerEntity) outerDataSet.get("entity")).getValueStr().equals("testStr");
    }

    private void testEntityCollectionMapping() {

        //List
        N2oObject.Parameter param = new N2oObject.Parameter();
        param.setId("entities");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        param.setPluralityType(PluralityType.list);
        N2oObject.Parameter childParam1 = new N2oObject.Parameter();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        N2oObject.Parameter childParam2 = new N2oObject.Parameter();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        param.setChildParams(new N2oObject.Parameter[]{childParam1, childParam2});

        DataSet innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 666);
        innerDataSet1.put("name", "testStr1");

        DataSet innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");

        List list = new ArrayList();
        list.add(innerDataSet1);
        list.add(innerDataSet2);

        DataSet outerDataSetWithList = new DataSet();
        outerDataSetWithList.put("entities", list);

        MappingProcessor.mapParameter(param, outerDataSetWithList);

        assert outerDataSetWithList.get("entities") instanceof List;
        assert ((TestEntity.InnerEntity) ((List) outerDataSetWithList.get("entities")).get(0)).getValueInt().equals(666);
        assert ((TestEntity.InnerEntity) ((List) outerDataSetWithList.get("entities")).get(0)).getValueStr().equals("testStr1");
        assert ((TestEntity.InnerEntity) ((List) outerDataSetWithList.get("entities")).get(1)).getValueInt().equals(777);
        assert ((TestEntity.InnerEntity) ((List) outerDataSetWithList.get("entities")).get(1)).getValueStr().equals("testStr2");


        //Set
        param = new N2oObject.Parameter();
        param.setId("entities");
        param.setEntityClass("net.n2oapp.framework.engine.util.TestEntity$InnerEntity");
        param.setPluralityType(PluralityType.set);
        childParam1 = new N2oObject.Parameter();
        childParam1.setId("id");
        childParam1.setMapping("valueInt");
        childParam2 = new N2oObject.Parameter();
        childParam2.setId("name");
        childParam2.setMapping("valueStr");
        param.setChildParams(new N2oObject.Parameter[]{childParam1, childParam2});

        innerDataSet1 = new DataSet();
        innerDataSet1.put("id", 666);
        innerDataSet1.put("name", "testStr1");

        innerDataSet2 = new DataSet();
        innerDataSet2.put("id", 777);
        innerDataSet2.put("name", "testStr2");

        Set set = new HashSet();
        set.add(innerDataSet1);
        set.add(innerDataSet2);

        DataSet outerDataSetWithSet = new DataSet();
        outerDataSetWithSet.put("entities", set);

        MappingProcessor.mapParameter(param, outerDataSetWithSet);

        assert outerDataSetWithSet.get("entities") instanceof Set;
        assert ((Set) outerDataSetWithSet.get("entities")).containsAll((List) outerDataSetWithList.get("entities"));
    }
}