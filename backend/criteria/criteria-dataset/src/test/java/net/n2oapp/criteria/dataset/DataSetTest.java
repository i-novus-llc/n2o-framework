package net.n2oapp.criteria.dataset;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {

    @Test
    void testSelfMerge() {
        DataSet dataSet = new DataSet("test", new ArrayList<>(Arrays.asList(1, 2)));
        dataSet.merge(dataSet);
        assertEquals(Arrays.asList(1, 2), dataSet.get("test"));
    }

    @Test
    void testMerge() {
        DataSet mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        mainDataSet.put("gender.id", 1);
        mainDataSet.put("gender.name", "Мужской");
        mainDataSet.put("address[0]", "kazan");

        DataSet extendDataSet = new DataSet();
        extendDataSet.put("id", 2);
        extendDataSet.put("gender.id", 2);
        extendDataSet.put("gender.name", "Мужской");
        extendDataSet.put("address[0]", "chistopol");

        mainDataSet.merge(extendDataSet);

        assertEquals(2, mainDataSet.get("id"));
        assertEquals(2, mainDataSet.get("gender.id"));
        assertEquals("Мужской", mainDataSet.get("gender.name"));

        List<?> address = (List<?>) mainDataSet.get("address");
        assertEquals(2, address.size());
        assertTrue(address.contains("kazan"));
        assertTrue(address.contains("chistopol"));
    }

    @Test
    void testWithStrategy() {
        DataSet mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        mainDataSet.put("gender.id", 1);

        DataSet extendDataSet = new DataSet();
        extendDataSet.put("id", 2);
        extendDataSet.put("gender.id", 2);
        extendDataSet.put("gender.name", "мужской");

        mainDataSet.merge(extendDataSet, (mainValue, extendValue) -> {
            if (mainValue != null)
                return extendValue;
            return null;
        });

        assertEquals(2, mainDataSet.get("id"));
        assertEquals(2, mainDataSet.get("gender.id"));
        assertNull(mainDataSet.get("gender.name"));

        //EXTEND_IF_VALUE_NOT_NULL
        mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        mainDataSet.put("gender.id", 1);

        extendDataSet = new DataSet();
        extendDataSet.put("id", 2);
        extendDataSet.put("gender.name", "мужской");

        mainDataSet.merge(extendDataSet, DataSet.EXTEND_IF_VALUE_NOT_NULL);

        assertEquals(2, mainDataSet.get("id"));
        assertEquals(1, mainDataSet.get("gender.id"));
        assertEquals("мужской", mainDataSet.get("gender.name"));
    }

    @Test
    void testArrayMergeStrategy() {
        DataSet mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        DataSet extendDataSet = new DataSet();
        extendDataSet.put("name", "Ivan");
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        extendDataSet.put("persons", list);

        // Добавляем первый раз со стратегией merge
        mainDataSet.merge(extendDataSet, ArrayMergeStrategyEnum.MERGE, true);
        assertEquals(1, mainDataSet.get("id"));
        assertEquals("Ivan", mainDataSet.get("name"));
        assertEquals(2, ((List<?>) mainDataSet.get("persons")).size());

        // Добавляем второй раз со стратегией merge, список должен увеличиться
        mainDataSet.merge(extendDataSet, ArrayMergeStrategyEnum.MERGE, true);
        assertEquals(4, ((List<?>) mainDataSet.get("persons")).size());
    }

    @Test
    void testArrayReplaceStrategy() {
        DataSet mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        mainDataSet.put("persons", list);

        DataSet extendDataSet = new DataSet();
        extendDataSet.put("name", "Ivan");
        List<String> list2 = new ArrayList<>();
        list2.add("third");
        list2.add("fourth");
        extendDataSet.put("persons", list2);

        mainDataSet.merge(extendDataSet, ArrayMergeStrategyEnum.REPLACE, true);
        assertEquals(1, mainDataSet.get("id"));
        assertEquals("Ivan", mainDataSet.get("name"));
        assertEquals(2, ((List<?>) mainDataSet.get("persons")).size());
        assertEquals("third", ((List<?>) mainDataSet.get("persons")).get(0));
        assertEquals("fourth", ((List<?>) mainDataSet.get("persons")).get(1));
    }

    @Test
    void cast1() {
        DataSet dataSet = new DataSet();
        dataSet.put("int", 1);
        dataSet.put("long", 100L);
        dataSet.put("str", "test");
        dataSet.put("bool", true);
        dataSet.put("data", Collections.singletonMap("key", "value"));
        dataSet.put("array", new Integer[]{1, 2, 3});
        dataSet.put("list", Arrays.asList(1, 2, 3));
        dataSet.put("set", Collections.singleton(1));

        assertEquals(1, dataSet.getInteger("int"));
        assertEquals(100L, dataSet.getLong("long"));
        assertEquals("test", dataSet.getString("str"));
        assertTrue(dataSet.getBoolean("bool"));
        assertEquals("value", dataSet.getDataSet("data").getString("key"));
        assertEquals(3, dataSet.getList("array").size());
        assertEquals(3, dataSet.getList("list").size());
        assertEquals(1, dataSet.getList("set").size());
    }

    @Test
    void cast2() {
        DataSet dataSet = new DataSet();
        dataSet.put("int", "1");
        dataSet.put("long", "100");
        dataSet.put("str", "test");
        dataSet.put("bool", "true");
        dataSet.put("data.key", "value");
        dataSet.put("list[0]", "1");
        dataSet.put("list[1]", "2");
        dataSet.put("list[2]", "3");

        assertEquals(1, dataSet.getInteger("int"));
        assertEquals(100L, dataSet.getLong("long"));
        assertEquals("test", dataSet.getString("str"));
        assertTrue(dataSet.getBoolean("bool"));
        assertEquals("value", dataSet.getDataSet("data").getString("key"));
        assertEquals(3, dataSet.getList("list").size());
    }
}
