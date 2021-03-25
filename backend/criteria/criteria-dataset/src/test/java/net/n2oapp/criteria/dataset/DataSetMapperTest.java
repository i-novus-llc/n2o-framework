package net.n2oapp.criteria.dataset;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
        assertThat(dataSet.get("id"), is(1));
        assertThat(dataSet.get("name"), is("test"));
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
        assertThat(dataSet.get("id"), is(1));
        assertThat(dataSet.get("name"), is("test"));
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
        assertThat(value.length, is(3));
        assertThat(value[0], is(1));
        assertThat(value[1], is("test"));
        assertThat(value[2], is("test"));
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
        assertThat(value.length, is(1));
        assertThat(value[0], instanceOf(Entity.class));
        assertThat(((Entity) value[0]).getId(), is(1));
        assertThat(((Entity) value[0]).getName(), is("test"));
    }

    @Test
    public void testMapToMap() {
        Map<String, FieldMapping> mapping = new HashMap<>();
        mapping.put("id", new FieldMapping("['id']"));
        mapping.put("name", new FieldMapping("['name']"));
        mapping.put("desc", null);

        FieldMapping refChild = new FieldMapping("['reference']");
        Map<String, FieldMapping> childMapping = new HashMap<>();
        refChild.setChildMapping(childMapping);
        mapping.put("ref", refChild);
        childMapping.put("id", new FieldMapping("['refId']"));
        childMapping.put("name", new FieldMapping("['refName']"));

        FieldMapping listChild = new FieldMapping("['persons']");
        childMapping = new HashMap<>();
        listChild.setChildMapping(childMapping);
        mapping.put("personList", listChild);
        childMapping.put("id", new FieldMapping("['personId']"));
        FieldMapping childMappingList = new FieldMapping("['personRatings']");
        childMappingList.setChildMapping(Map.of("ratingValue", new FieldMapping("['value']")));
        childMapping.put("ratings", childMappingList);
        FieldMapping childMappingSet = new FieldMapping("['personCodes']");
        childMapping.put("codes", childMappingSet);
        childMappingSet.setChildMapping(Map.of("codeValue", new FieldMapping("['value']")));

        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        dataSet.put("name", "test");
        dataSet.put("desc", "test");
        DataSet refDataSet = new DataSet();
        refDataSet.put("id", 123);
        refDataSet.put("name", "Joe");
        dataSet.put("ref", refDataSet);
        // list in list
        List listItemList = Arrays.asList(new DataSet("ratingValue", 2.34), new DataSet("ratingValue", 5.55));
        DataSet listItem = new DataSet("id", 1);
        listItem.add("ratings", listItemList);
        List listDataSet = Arrays.asList(listItem);
        dataSet.put("personList", listDataSet);
        // set in list
        Set<DataSet> listItemSet = new HashSet();
        listItemSet.add(new DataSet("codeValue", "code1"));
        listItemSet.add(new DataSet("codeValue", "code2"));
        listItem.add("codes", listItemSet);

        Map<String, Object> value = DataSetMapper.mapToMap(dataSet, mapping);
        assertThat(value.size(), is(5));
        assertThat(value.get("id"), is(1));
        assertThat(value.get("name"), is("test"));
        assertThat(value.get("desc"), is("test"));
        DataSet reference = (DataSet) value.get("reference");
        assertThat(reference.size(), is(2));
        assertThat(reference.get("refId"), is(123));
        assertThat(reference.get("refName"), is("Joe"));
        List list = (List) value.get("persons");
        assertThat(list.size(), is(1));
        assertThat(((DataSet) list.get(0)).size(), is(3));
        assertThat(((DataSet) list.get(0)).get("personId"), is(1));
        List personRatings = (List) ((DataSet) list.get(0)).get("personRatings");
        assertThat(personRatings.size(), is(2));
        assertThat(((DataSet) personRatings.get(0)).get("value"), is(2.34));
        assertThat(((DataSet) personRatings.get(1)).get("value"), is(5.55));
        HashSet personCodes = (HashSet) ((DataSet) list.get(0)).get("personCodes");
        assertThat(personCodes.size(), is(2));
        assertThat(personCodes, is(Set.of(new DataSet("value", "code1"), new DataSet("value", "code2"))));
    }

    @Test
    public void testPrimitiveMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("id", "[0]");
        DataSet dataSet = new DataSet("id", 2L);
        Object[] map = DataSetMapper.map(dataSet, mapping, "java.lang.Long");

        assertThat(map[0], instanceOf(Long.class));
        assertThat(map[0], is(2L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapToMapThrowIllegalArgs() {
        DataSet dataSet = new DataSet();
        dataSet.put("surname", "Alexeev");
        dataSet.put("name", "Vladimir");
        dataSet.put("id", 1);

        Map<String, FieldMapping> mapping = new HashMap<>();
        mapping.put("id", new FieldMapping("[0]"));
        mapping.put("name", new FieldMapping("[1]"));
        mapping.put("surname", new FieldMapping("[2]"));

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
