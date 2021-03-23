package net.n2oapp.criteria.dataset;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: iryabov
 * Date: 17.02.13
 * Time: 14:09
 */
public class DataSetMapperTest {
    @Test
    public void testExtractFromArray() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "[0]");
        mapping.put("name", "[1]");
        Object[] source = new Object[]{1, "test"};
        DataSet dataSet = DataSetMapper.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }

    @Test
    public void testExtractFromEntity() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "id");
        mapping.put("name", "name");
        Entity source = new Entity();
        source.setId(1);
        source.setName("test");
        DataSet dataSet = DataSetMapper.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }

    @Test
    public void testMapToArray() {
        Map<String, String> mapping = new LinkedHashMap<>();
        mapping.put("id", "[0]");
        mapping.put("name", "[1]");
        mapping.put("desc", null);
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        dataSet.put("desc", "test");
        Object[] value = DataSetMapper.map(dataSet, mapping);
        assert value.length == 3;
        assert value[0].equals(1);
        assert value[1].equals("test");
        assert value[2].equals("test");
    }

    @Test
    public void testMapToArrayEntity() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "[0].id");
        mapping.put("name", "[0].name");
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        Object[] value = DataSetMapper.map(dataSet, mapping, Entity.class.getName());
        assert value.length == 1;
        assert value[0] instanceof Entity;
        assert ((Entity) value[0]).getId().equals(1);
        assert ((Entity) value[0]).getName().equals("test");
    }

    @Test
    public void testMapToMap() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "['id']");
        mapping.put("name", "['name']");
        mapping.put("desc", null);
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        dataSet.put("desc", "test");
        Map<String, Object> value = DataSetMapper.mapToMap(dataSet, mapping);
        assert value.size() == 3;
        assert value.get("id").equals(1);
        assert value.get("name").equals("test");
        assert value.get("desc").equals("test");
    }

    @Test
    public void testPrimitiveMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "[0]");
        DataSet dataSet = new DataSet("id", 2L);
        Object[] map = DataSetMapper.map(dataSet, mapping, "java.lang.Long");

        assert map[0] instanceof Long;
        assert map[0].equals(2L);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToMapThrowIllegalArgs() {
        DataSet dataSet = new DataSet();
        dataSet.put("surname", "Alexeev");
        dataSet.put("name", "Vladimir");
        dataSet.put("id", 1);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "[0]");
        mapping.put("name", "[1]");
        mapping.put("surname", "[2]");

        DataSetMapper.mapToMap(dataSet, mapping);
    }

    public static class Entity {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
