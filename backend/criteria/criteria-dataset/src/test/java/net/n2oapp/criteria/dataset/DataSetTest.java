package net.n2oapp.criteria.dataset;

import org.junit.Test;
import net.n2oapp.criteria.dataset.DataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: operhod
 * Date: 14.07.14
 * Time: 17:25
 */
public class DataSetTest {


    @Test
    public void testSelfMerge() {
        DataSet dataSet = new DataSet("test", new ArrayList<>(Arrays.asList(1, 2)));
        dataSet.merge(dataSet);
        assert dataSet.get("test").equals(Arrays.asList(1, 2));
    }

    @Test
    public void testMerge() {
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

        assert mainDataSet.get("id").equals(2);
        assert mainDataSet.get("gender.id").equals(2);
        assert mainDataSet.get("gender.name").equals("Мужской");
        List address = (List) mainDataSet.get("address");
        assert address.size() == 2;
        assert address.contains("kazan");
        assert address.contains("chistopol");
    }

    @Test
    public void testWithStrategy() {

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

        assert mainDataSet.get("id").equals(2);
        assert mainDataSet.get("gender.id").equals(2);
        assert mainDataSet.get("gender.name") == null;

        //EXTEND_IF_VALUE_NOT_NULL
        mainDataSet = new DataSet();
        mainDataSet.put("id", 1);
        mainDataSet.put("gender.id", 1);


        extendDataSet = new DataSet();
        extendDataSet.put("id", 2);
        extendDataSet.put("gender.name", "мужской");


        mainDataSet.merge(extendDataSet, DataSet.EXTEND_IF_VALUE_NOT_NULL);

        assert mainDataSet.get("id").equals(2);
        assert mainDataSet.get("gender.id").equals(1);
        assert mainDataSet.get("gender.name").equals("мужской");


    }

    @Test
    public void cast1() {
        DataSet dataSet = new DataSet();
        dataSet.put("int", 1);
        dataSet.put("long", 100L);
        dataSet.put("str", "test");
        dataSet.put("bool", true);
        dataSet.put("data", Collections.singletonMap("key", "value"));
        dataSet.put("array", new Integer[]{1, 2, 3});
        dataSet.put("list", Arrays.asList(1, 2, 3));
        dataSet.put("set", Collections.singleton(1));

        assert dataSet.getInteger("int") == 1;
        assert dataSet.getLong("long") == 100L;
        assert dataSet.getString("str").equals("test");
        assert dataSet.getBoolean("bool");
        assert dataSet.getDataSet("data").getString("key").equals("value");
        assert dataSet.getList("array").size() == 3;
        assert dataSet.getList("list").size() == 3;
        assert dataSet.getList("set").size() == 1;
    }

    @Test
    public void cast2() {
        DataSet dataSet = new DataSet();
        dataSet.put("int", "1");
        dataSet.put("long", "100");
        dataSet.put("str", "test");
        dataSet.put("bool", "true");
        dataSet.put("data.key", "value");
        dataSet.put("list[0]", "1");
        dataSet.put("list[1]", "2");
        dataSet.put("list[2]", "3");

        assert dataSet.getInteger("int") == 1;
        assert dataSet.getLong("long") == 100L;
        assert dataSet.getString("str").equals("test");
        assert dataSet.getBoolean("bool");
        assert dataSet.getDataSet("data").getString("key").equals("value");
        assert dataSet.getList("list").size() == 3;
    }
}
