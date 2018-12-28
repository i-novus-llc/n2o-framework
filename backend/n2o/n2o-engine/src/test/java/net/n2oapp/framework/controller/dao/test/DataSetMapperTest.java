package net.n2oapp.framework.controller.dao.test;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetMapper;
import org.junit.Test;

import java.util.HashMap;
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
        Foo source = new Foo();
        source.setId(1);
        source.setName("test");
        DataSet dataSet = DataSetMapper.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }

    @Test
    public void testExtractFromVolumeEntity() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "foo.id");
        mapping.put("name", "foo.name");
        Foo foo = new Foo();
        foo.setId(1);
        foo.setName("test");
        Bar source = new Bar();
        source.setFoo(foo);
        DataSet dataSet = DataSetMapper.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }

    @Test
    public void testMapToArray() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "[0]");
        mapping.put("name", "[1]");
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        Object[] value = DataSetMapper.map(dataSet, mapping);
        assert value.length == 2;
        assert value[0].equals(1);
        assert value[1].equals("test");
    }

    @Test
    public void testMapToEntity() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "[0].id");
        mapping.put("name", "[0].name");
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        Object[] value = DataSetMapper.map(dataSet, mapping, Foo.class.getName());
        assert value.length == 1;
        assert value[0] instanceof Foo;
        assert ((Foo) value[0]).getId().equals(1);
        assert ((Foo) value[0]).getName().equals("test");
    }

    @Test
    public void testMapToVolumeEntity() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "[0].foo.id");
        mapping.put("name", "[0].foo.name");
        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        Object[] value = DataSetMapper.map(dataSet, mapping, Bar.class.getName());
        assert value.length == 1;
        assert value[0] instanceof Bar;
        assert ((Bar) value[0]).getFoo().getId().equals(1);
        assert ((Bar) value[0]).getFoo().getName().equals("test");
    }

    public static class Bar {
        private Foo foo;

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }
    }

    public static class Foo {
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
