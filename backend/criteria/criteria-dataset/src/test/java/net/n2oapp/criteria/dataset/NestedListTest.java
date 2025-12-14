package net.n2oapp.criteria.dataset;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class NestedListTest {

    @Test
    void get() {
        NestedList list = new NestedList();
        list.add(1);
        assertEquals(1, list.get("[0]"));

        list = new NestedList();
        Map<String, Object> map = new HashMap<>();
        map.put("foo", 1);
        list.add(map);
        assertEquals(1, list.get("[0].foo"));

        list = new NestedList();
        List<Object> nested = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("foo", 1);
        nested.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("foo", 2);
        nested.add(map2);
        list.add(nested);
        assertInstanceOf(List.class, list.get("[0]*.foo"));
        List<?> fooList = (List<?>) list.get("[0]*.foo");
        assertEquals(1, fooList.get(0));
        assertEquals(2, fooList.get(1));

        list = new NestedList();
        nested = new NestedList();
        nested.add(1);
        list.add(nested);
        assertEquals(1, list.get("[0][0]"));

        //empty
        list = new NestedList();
        list.add(1);
        assertNull(list.get("[1]"));//safe index of bound

        list = new NestedList();
        list.add(null);
        assertNull(list.get("[0].foo"));//safe get on null

        list = new NestedList();
        list.add(1);
        assertNull(list.get("[0].foo"));//not a map

        list = new NestedList();
        list.add(1);
        assertNull(list.get("[0]*.foo"));//not a list
    }

    @Test
    void put() {
        NestedList list = new NestedList();
        list.put("[1]", 1);
        assertEquals(1, list.get(1));

        list = new NestedList();
        list.put("[1].foo", 1);
        assertInstanceOf(Map.class, list.get(1));
        assertEquals(1, ((Map<?, ?>) list.get(1)).get("foo"));

        list = new NestedList();
        list.put("[1][2]", 1);
        assertInstanceOf(List.class, list.get(1));
        assertEquals(1, ((List<?>) list.get(1)).get(2));

        list = new NestedList();
        list.put("[1]*.foo", Arrays.asList(1, 2, 3));
        assertInstanceOf(List.class, list.get(1));
        assertInstanceOf(Map.class, ((List<?>) list.get(1)).get(0));
        assertEquals(1, ((Map<?, ?>) ((List<?>) list.get(1)).get(0)).get("foo"));

        //negative
        list = new NestedList();
        list.put("[0]", 1);
        assertEquals(1, list.put("[0]*.foo", null));
        assertNull(list.get(0));

        NestedList list2 = new NestedList();
        assertThrows(IllegalArgumentException.class, () -> list2.put("[1]*.foo", 1)); //value not an iterable
    }

    @Test
    void create() {
        NestedList list = new NestedList(Arrays.asList(
                1,
                Arrays.asList(1, 2),
                Collections.singletonMap("foo", 1)));
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertInstanceOf(NestedList.class, list.get(1));
        assertInstanceOf(NestedMap.class, list.get(2));
    }

    @Test
    void negative() {
        NestedList list = new NestedList();
        assertThrows(IllegalArgumentException.class, () -> list.put(null, 1));//null key
        assertThrows(IllegalArgumentException.class, () -> list.put("", 1));//empty key
        assertThrows(IllegalArgumentException.class, () -> list.put("0", 1));//there isn't a '['
        assertThrows(IllegalArgumentException.class, () -> list.put("[0", 1));//there isn't a ']'
        assertThrows(IllegalArgumentException.class, () -> list.put("[foo]", 1));//not a numeric
        assertThrows(IllegalArgumentException.class, () -> list.put("[0].123", 1));//not a java var
        assertThrows(IllegalArgumentException.class, () -> list.put("[0]*.123", 1));//not a java var in spread
        assertThrows(IllegalArgumentException.class, () -> list.put("[0]123", 1));//there isn't a '.'
    }

    @Test
    void testResolveObjectValue() {
        // Test with simple Java object - direct property access
        NestedList list = new NestedList();
        TestObject testObj = new TestObject();
        testObj.setName("Иван");
        testObj.setAge(30);
        list.add(testObj);

        assertEquals("Иван", list.get("[0].name"));
        assertEquals(30, list.get("[0].age"));

        // Test with nested object property
        TestObject nestedObj = new TestObject();
        nestedObj.setName("Мария");
        testObj.setNested(nestedObj);
        assertEquals("Мария", list.get("[0].nested.name"));

        // Test with non-existent property - should return null
        assertNull(list.get("[0].nonExistent"));

        // Test with array/list property access
        TestObject objWithList = new TestObject();
        objWithList.setTags(Arrays.asList("tag1", "tag2"));
        list.add(objWithList);
        assertEquals("tag1", list.get("[1].tags[0]"));
        assertEquals("tag2", list.get("[1].tags[1]"));

        // Test with method call
        assertEquals("ИВАН", list.get("[0].name.toUpperCase()"));

        // Test with multiple objects
        TestObject secondObj = new TestObject();
        secondObj.setName("Петр");
        list.add(secondObj);
        assertEquals("Петр", list.get("[2].name"));
    }

    @Test
    void testResolveObjectValueWithExceptions() {
        NestedList list = new NestedList();
        TestObject testObj = new TestObject();
        testObj.setName("Иван");
        list.add(testObj);

        // Test that PROPERTY_OR_FIELD_NOT_READABLE returns null (not throws)
        assertNull(list.get("[0].privateField"));

        // Test that other SpEL errors are thrown
        assertThrows(Exception.class, () -> list.get("[0].invalidExpression("));
    }

    private boolean fail(Supplier<Object> test, Class<? extends Exception> exClass) {
        try {
            test.get();
            return false;
        } catch (Exception e) {
            if (e.getClass().equals(exClass)) {
                return true;
            }
            throw e;
        }
    }

    @Getter
    @Setter
    static class TestObject {
        private String name;
        private int age;
        private TestObject nested;
        private java.util.List<String> tags;
        @Getter(value = AccessLevel.NONE)
        private String privateField = "private";
    }
}
