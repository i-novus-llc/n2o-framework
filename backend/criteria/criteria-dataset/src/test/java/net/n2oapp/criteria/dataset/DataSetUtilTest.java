package net.n2oapp.criteria.dataset;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class DataSetUtilTest {

    @Test
    public void testExtractFromArray() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "[0]");
        mapping.put("name", "[1]");
        Object[] source = new Object[]{1, "test"};
        DataSet dataSet = DataSetUtil.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }

    @Test
    public void testExtractFromEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "id");
        mapping.put("name", "name");
        mapping.put("age", null);
        Foo source = new Foo();
        source.setId(1);
        source.setName("test");
        source.setAge(25);
        DataSet dataSet = DataSetUtil.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
        assert dataSet.get("age").equals(25);
    }

    @Test
    public void testExtractFromVolumeEntity() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "foo.id");
        mapping.put("name", "foo.name");
        Foo foo = new Foo();
        foo.setId(1);
        foo.setName("test");
        Bar source = new Bar();
        source.setFoo(foo);
        DataSet dataSet = DataSetUtil.extract(source, mapping);
        assert dataSet.get("id").equals(1);
        assert dataSet.get("name").equals("test");
    }


    @Getter
    @Setter
    public static class Bar {
        private Foo foo;
    }

    @Getter
    @Setter
    public static class Foo {
        private Integer id;
        private String name;
        private Integer age;
    }
}
