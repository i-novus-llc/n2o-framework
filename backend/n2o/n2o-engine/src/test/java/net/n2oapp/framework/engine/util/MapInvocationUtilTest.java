package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MapInvocationUtilTest {

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
        DataList listItemSet = new DataList();
        listItemSet.add(new DataSet("codeValue", "code1"));
        listItemSet.add(new DataSet("codeValue", "code2"));
        listItem.add("codes", listItemSet);

        Map<String, Object> value = MapInvocationUtil.mapToMap(dataSet, mapping);
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
        DataList personCodes = (DataList) ((DataSet) list.get(0)).get("personCodes");
        assertThat(personCodes.size(), is(2));
        assertThat(personCodes.contains(new DataSet("value", "code1")), is(true));
        assertThat(personCodes.contains(new DataSet("value", "code2")), is(true));
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

        MapInvocationUtil.mapToMap(dataSet, mapping);
    }
}
