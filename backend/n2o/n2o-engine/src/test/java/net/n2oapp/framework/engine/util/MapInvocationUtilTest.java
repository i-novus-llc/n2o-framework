package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapInvocationUtilTest {

    @Test
    void testMapToMap() {
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

        FieldMapping nullList = new FieldMapping("['null_list']");
        nullList.setChildMapping(Map.of("nullChild", new FieldMapping("['null_child']")));
        mapping.put("null_list", nullList);

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
        List listDataSet = List.of(listItem);
        dataSet.put("personList", listDataSet);
        // set in list
        DataList listItemSet = new DataList();
        listItemSet.add(new DataSet("codeValue", "code1"));
        listItemSet.add(new DataSet("codeValue", "code2"));
        listItem.add("codes", listItemSet);

        Map<String, Object> value = MapInvocationUtil.mapToMap(dataSet, mapping);
        assertEquals(6, value.size());
        assertEquals(1, value.get("id"));
        assertEquals("test", value.get("name"));
        assertEquals("test", value.get("desc"));
        DataSet reference = (DataSet) value.get("reference");
        assertEquals(2, reference.size());
        assertEquals(123, reference.get("refId"));
        assertEquals("Joe", reference.get("refName"));
        List list = (List) value.get("persons");
        assertEquals(1, list.size());
        assertEquals(3, ((DataSet) list.get(0)).size());
        assertEquals(1, ((DataSet) list.get(0)).get("personId"));
        List personRatings = (List) ((DataSet) list.get(0)).get("personRatings");
        assertEquals(2, personRatings.size());
        assertEquals(2.34, ((DataSet) personRatings.get(0)).get("value"));
        assertEquals(5.55, ((DataSet) personRatings.get(1)).get("value"));
        DataList personCodes = (DataList) ((DataSet) list.get(0)).get("personCodes");
        assertEquals(2, personCodes.size());
        assertTrue(personCodes.contains(new DataSet("value", "code1")));
        assertTrue(personCodes.contains(new DataSet("value", "code2")));
        assertTrue(value.containsKey("null_list"));
        assertNull(value.get("null_list"));
    }

    @Test
    void testMapToMapThrowIllegalArgs() {
        assertThrows(N2oSpelException.class, () -> {
            DataSet dataSet = new DataSet();
            dataSet.put("surname", "Alexeev");
            dataSet.put("name", "Vladimir");
            dataSet.put("id", 1);

            Map<String, FieldMapping> mapping = new HashMap<>();
            mapping.put("id", new FieldMapping("[0]"));
            mapping.put("name", new FieldMapping("[1]"));
            mapping.put("surname", new FieldMapping("[2]"));

            MapInvocationUtil.mapToMap(dataSet, mapping);
        });
    }
}
