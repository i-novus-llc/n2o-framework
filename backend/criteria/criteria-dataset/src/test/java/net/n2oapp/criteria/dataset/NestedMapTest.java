package net.n2oapp.criteria.dataset;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class NestedMapTest {

    @Test
    void get() {
        NestedMap map = new NestedMap();
        map.put("foo", 1);
        assertEquals(1, map.get("foo"));
        assertEquals(1, map.get("['foo']"));
        assertEquals(1, map.get("[\"foo\"]"));

        map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("bar", 1);
        map.put("foo", map2);
        assertEquals(1, map.get("foo.bar"));
        assertEquals(1, map.get("['foo'].bar"));
        assertEquals(1, map.get("[\"foo\"].bar"));
        assertEquals(1, map.get("foo['bar']"));
        assertEquals(1, map.get("foo[\"bar\"]"));
        assertEquals(1, map.get("['foo']['bar']"));
        assertEquals(1, map.get("[\"foo\"][\"bar\"]"));

        map = new NestedMap();
        List<Object> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("bar", 1);
        list.add(map1);
        map2 = new HashMap<>();
        map2.put("bar", 2);
        list.add(map2);
        map.put("foo", list);

        assertTrue(map.get("foo*.bar") instanceof List);
        assertTrue(map.get("['foo']*.bar") instanceof List);
        assertEquals(1, ((List<?>) map.get("foo*.bar")).get(0));
        assertEquals(2, ((List<?>) map.get("foo*.bar")).get(1));

        map = new NestedMap();
        list = new NestedList();
        list.add(1);
        map.put("foo", list);
        assertEquals(1, map.get("foo[0]"));

        //empty
        map = new NestedMap();
        assertNull(map.get("bar"));//safe not key

        map = new NestedMap();
        map.put("foo", null);
        assertNull(map.get("foo.bar"));//safe get on null

        map = new NestedMap();
        map.put("foo", 1);
        assertNull(map.get("foo.bar"));//not a map

        map = new NestedMap();
        map.put("foo", 1);
        assertNull(map.get("foo*.bar"));//not a list

        //negative
        assertTrue(fail(() -> new NestedMap().get(null), IllegalArgumentException.class));//key is null
    }

    @Test
    void put() {
        NestedMap map = new NestedMap();
        map.put("foo", 1);
        assertEquals(1, map.get("foo"));

        map = new NestedMap();
        map.put("['foo']", 1);
        assertEquals(1, map.get("foo"));

        map = new NestedMap();
        map.put("[\"foo\"]", 1);
        assertEquals(1, map.get("foo"));

        map = new NestedMap();
        map.put("foo.bar", 1);
        assertTrue(map.get("foo") instanceof Map);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("bar"));

        map = new NestedMap();
        map.put("foo['bar']", 1);
        assertTrue(map.get("foo") instanceof Map);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("bar"));

        map = new NestedMap();
        map.put("['foo']['bar']", 1);
        assertTrue(map.get("foo") instanceof Map);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("bar"));

        map = new NestedMap();
        map.put("foo[2]", 1);
        assertTrue(map.get("foo") instanceof List);
        assertEquals(1, ((List<?>) map.get("foo")).get(2));

        map = new NestedMap();
        map.put("foo*.bar", Arrays.asList(1, 2, 3));
        assertTrue(map.get("foo") instanceof List);
        assertTrue(((List<?>) map.get("foo")).get(0) instanceof Map);
        assertEquals(2, ((Map<?, ?>) ((List<?>) map.get("foo")).get(1)).get("bar"));

        //put map
        map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("bar", 1);
        map.put("foo", map2);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("bar"));

        //put map 2
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("bar.test", 1);
        map.put("foo", map2);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("['bar.test']"));

        //put map 3
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("bar['test']", 1);
        map.put("foo", map2);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("[\"bar['test']\"]"));

        //put map 4
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("2019-01-01", 1);
        map.put("foo", map2);
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("['2019-01-01']"));

        //negative
        map = new NestedMap();
        map.put("foo", 1);
        assertEquals(1, map.put("foo*.bar", null));
        assertNull(map.get("foo"));

        NestedMap map3 = new NestedMap();
        assertTrue(fail(() -> map3.put("foo*.bar", 1), IllegalArgumentException.class));//value not an iterable
    }

    @Test
    void putAll() {
        NestedMap map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("foo", 1);
        map.putAll(map2);
        assertEquals(1, map.get("foo"));

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("1", 1);
        map.putAll(map2);
        assertEquals(1, map.get("['1']"));

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo.bar", 1);
        map.putAll(map2);
        assertEquals(1, map.get("['foo.bar']"));
        assertNull(map.get("foo"));

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo", Arrays.asList(1, 2, 3));
        map.putAll(map2);
        assertEquals(3, ((List<?>) map.get("foo")).size());

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("2019-01-01", 1);
        map.putAll(map2);
        assertEquals(1, map.get("['2019-01-01']"));

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo[0]", 1);
        map.putAll(map2);
        assertEquals(1, map.get("['foo[0]']"));
        assertNull(map.get("foo"));

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo['bar']", 1);
        map.putAll(map2);
        assertEquals(1, map.get("[\"foo['bar']\"]"));
        assertNull(map.get("foo"));
    }

    @Test
    void removeAndContains() {
        // Test basic key formats
        testScenario("foo", "foo");
        testScenario("foo", "['foo']");
        testScenario("foo", "[\"foo\"]");

        // Test nested key formats
        testScenario("foo.bar", "foo.bar");
        testScenario("foo.bar", "foo['bar']");
        testScenario("foo.bar", "['foo']['bar']");

        // Test array index
        testScenario("foo[2]", "foo[2]");

        // Test cascade remove
        NestedMap map = new NestedMap();
        map.put("foo.bar", 1);
        assertTrue(map.containsKey("foo"));
        assertTrue(map.remove("foo") instanceof Map);
        assertFalse(map.containsKey("foo"));

        map = new NestedMap();
        map.put("foo[0]", 1);
        assertTrue(map.containsKey("foo"));
        assertTrue(map.remove("foo") instanceof List);
        assertFalse(map.containsKey("foo"));

        // Test empty map
        map = new NestedMap();
        assertFalse(map.containsKey("foo"));
        assertNull(map.remove("foo"));

        // Test non-existing keys
        map = new NestedMap();
        map.put("foo[1]", 1);
        assertFalse(map.containsKey("foo[0]"));
        assertNull(map.remove("foo[0]"));

        map = new NestedMap();
        map.put("foo.bar", 1);
        assertFalse(map.containsKey("foo[0]"));
        assertNull(map.remove("foo[0]"));
        assertTrue(map.containsKey("foo.bar"));//not replace

        map = new NestedMap();
        map.put("foo", Arrays.asList(1, 2, 3));
        assertFalse(map.containsKey("foo.bar"));
        assertNull(map.remove("foo.bar"));
        assertTrue(map.containsKey("foo[0]"));//not replace
    }

    private void testScenario(String putKey, String testKey) {
        NestedMap map = new NestedMap();
        map.put(putKey, 1);
        assertTrue(map.containsKey(testKey));
        assertEquals(1, map.remove(testKey));
        assertFalse(map.containsKey(testKey));
    }

    @Test
    void testInvalidKeys() {
        NestedMap map2 = new NestedMap();
        map2.put("foo.bar", 1);
        assertTrue(fail(() -> map2.containsKey("foo*.bar"), IllegalArgumentException.class));//value not an iterable
        assertTrue(fail(() -> map2.remove("foo*.bar"), IllegalArgumentException.class));//value not an iterable
        assertTrue(fail(() -> map2.containsKey(123), IllegalArgumentException.class));//value not an iterable
        assertTrue(fail(() -> map2.remove(123), IllegalArgumentException.class));//value not an iterable
    }

    @Test
    void remove() {
        NestedMap map = new NestedMap();
        map.put("id", 1);
        assertEquals(1, map.remove("id"));
        assertFalse(map.containsKey("id"));

        //remove not exists
        assertNull(map.remove("a.b"));
    }

    //Synthetic tests

    @Test
    void size() {
        NestedMap map = new NestedMap();
        map.put("id", 1);
        assertEquals(1, map.size());
        map.put("id2", 2);
        assertEquals(2, map.size());
    }

    @Test
    void invalidPut() {
        NestedMap map = new NestedMap();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> map.put(null, 1));
        assertNotNull(e.getMessage());

        assertThrows(IllegalArgumentException.class, () -> map.put("", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put(".", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a.b.", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put(".a.b", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a.b..", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a[0", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a.[0]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a[b]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a['b']c", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a['b\"]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a[\"b']", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("a['b]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("[a]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("['a]", 1));
        assertThrows(IllegalArgumentException.class, () -> map.put("['a\"]", 1));
    }

    @Test
    void invalidGet() {
        final NestedMap map = new NestedMap();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> map.get(null));
        assertNotNull(e.getMessage());

        assertThrows(IllegalArgumentException.class, () -> map.get("."));
        assertThrows(IllegalArgumentException.class, () -> map.get(".a"));

        map.put("a.b", 1);
        assertThrows(IllegalArgumentException.class, () -> map.get("a.."));

        final NestedMap map1 = new NestedMap();

        map1.put("a", Arrays.asList(1, 2));
        assertThrows(IllegalArgumentException.class, () -> map1.get("a[0"));

        final NestedMap map2 = new NestedMap();
        assertThrows(IllegalArgumentException.class, () -> map2.get("[0"));
        assertThrows(IllegalArgumentException.class, () -> map2.get("[a]"));
        assertThrows(IllegalArgumentException.class, () -> map2.get("a.1"));
        assertThrows(IllegalArgumentException.class, () -> map2.get("a*.1"));
    }

    /**
     * Example props
     */
    @Test
    void props() {
        NestedMap map;
        //simple props
        map = new NestedMap();
        assertNull(map.put("a", 1));
        assertTrue(map.containsKey("a"));
        assertEquals(1, map.get("a"));
        assertEquals(1, map.remove("a"));
        assertFalse(map.containsKey("a"));
        assertNull(map.get("a"));

        //nested props
        map = new NestedMap();
        assertNull(map.put("a.b", 1));
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("a.b"));
        assertTrue(map.get("a") instanceof NestedMap);
        assertEquals(1, map.get("a.b"));
        assertFalse(map.containsKey("a.b.c"));
        assertNull(map.get("a.b.c"));

        assertEquals(1, map.remove("a.b"));
        assertFalse(map.containsKey("a.b"));
        assertNull(map.get("a.b"));

        assertTrue(map.remove("a") instanceof NestedMap);
        assertFalse(map.containsKey("a"));
        assertNull(map.get("a"));
    }

    /**
     * Example map array
     */
    @Test
    void mapArray() {
        NestedMap map;
        //simple map
        map = new NestedMap();
        assertNull(map.put("['a']", 1));
        assertTrue(map.containsKey("['a']"));
        assertEquals(1, map.get("['a']"));
        assertEquals(1, map.remove("['a']"));
        assertFalse(map.containsKey("['a']"));
        assertNull(map.get("['a']"));

        //map or props
        map = new NestedMap();
        assertNull(map.put("['a']", 1));
        assertTrue(map.containsKey("a"));
        assertEquals(1, map.get("a"));
        assertEquals(1, map.remove("a"));
        assertNull(map.put("b", 2));
        assertTrue(map.containsKey("['b']"));
        assertEquals(2, map.get("['b']"));
        assertEquals(2, map.remove("['b']"));

        //map or props 2
        map = new NestedMap();
        assertNull(map.put("['a.b']", 1));
        assertTrue(map.containsKey("['a.b']"));
        assertEquals(1, map.get("['a.b']"));
        assertFalse(map.containsKey("['a']"));
        assertFalse(map.containsKey("a"));
        assertFalse(map.containsKey("a.b"));
        assertFalse(map.containsKey("['a'].b"));
    }

    @Test
    void mapAndProps() {
        //map and props
        NestedMap map;
        map = new NestedMap();
        assertNull(map.put("a['b']", 1));
        assertTrue(map.get("a") instanceof NestedMap);
        assertEquals(1, map.get("a['b']"));

        map = new NestedMap();
        assertNull(map.put("a['b'].c", 1));
        assertTrue(map.get("a") instanceof NestedMap);
        assertTrue(map.get("a['b']") instanceof NestedMap);
        assertEquals(1, map.get("a['b'].c"));

        map = new NestedMap();
        assertNull(map.put("['a']['b'].c", 1));
        assertTrue(map.get("['a']") instanceof NestedMap);
        assertTrue(map.get("['a']['b']") instanceof NestedMap);
        assertEquals(1, map.get("['a']['b'].c"));

        //map and props 2
        map = new NestedMap();
        assertNull(map.put("a.b", 1));
        assertEquals(1, map.get("a['b']"));
        assertEquals(1, map.get("['a']['b']"));
        assertEquals(1, map.get("['a'].b"));
        assertEquals(1, map.get("a.b"));
    }

    /**
     * Example index array
     */
    @Test
    void simpleIndexArray() {
        NestedMap map;
        //simple index
        map = new NestedMap();
        assertNull(map.put("a[0]", 1));
        assertNull(map.put("a[1]", 2));
        assertNull(map.put("a[2]", 3));
        assertTrue(map.containsKey("a"));
        assertTrue(map.get("a") instanceof List);
        assertEquals(3, ((List<?>) map.get("a")).size());
        assertTrue(map.containsKey("a[0]"));
        assertEquals(1, map.get("a[0]"));
        assertTrue(map.containsKey("a[1]"));
        assertEquals(2, map.get("a[1]"));
        assertTrue(map.containsKey("a[2]"));
        assertEquals(3, map.get("a[2]"));
        assertFalse(map.containsKey("a[3]"));
        assertEquals(2, map.remove("a[1]"));//after remove array size became is 2
        assertEquals(2, ((List<?>) map.get("a")).size());
        assertFalse(map.containsKey("a[3]"));
    }

    @Test
    void indexArray() {
        //nested index
        NestedMap map = new NestedMap();
        assertNull(map.put("a[0][1]", 1));
        assertTrue(map.containsKey("a"));
        assertTrue(map.get("a") instanceof List);
        assertTrue(map.get("a[0]") instanceof List);
        assertNull(map.get("a[1]"));
        assertNull(map.get("a[0][0]"));
        assertEquals(1, map.get("a[0][1]"));

        //props and index
        map = new NestedMap();
        assertNull(map.put("a[0].b", 1));
        assertNull(map.put("a[1].b", 2));
        assertNull(map.put("a[1].c", 3));
        assertTrue(map.containsKey("a"));
        assertTrue(map.get("a") instanceof List);
        assertTrue(map.get("a[0]") instanceof NestedMap);
        assertEquals(1, map.get("a[0].b"));
        assertEquals(2, map.get("a[1].b"));
        assertEquals(3, map.get("a[1].c"));

        //map and index
        map = new NestedMap();
        assertNull(map.put("['a'][0]", 1));
        assertTrue(map.get("['a']") instanceof List);
        assertEquals(1, map.get("['a'][0]"));

        //map and index and props
        map = new NestedMap();
        assertNull(map.put("['a'][0].b", 1));
        assertTrue(map.get("['a']") instanceof List);
        assertTrue(map.get("['a'][0]") instanceof NestedMap);
        assertEquals(1, map.get("['a'][0].b"));
    }

    @Test
    void testGetPutNested() {
        NestedMap map = new NestedMap();
        assertNull(map.put("foo.id", 1));
        assertEquals(1, map.put("foo.id", 1));
        assertNull(map.put("foo.name", "test"));
        assertTrue(map.containsKey("foo.id"));
        assertEquals(1, map.get("foo.id"));
        assertTrue(map.containsKey("foo.name"));
        assertEquals("test", map.get("foo.name"));
        assertTrue(map.containsKey("foo"));
        assertNotNull(map.get("foo"));
        assertTrue(map.get("foo") instanceof Map);
        assertTrue(((Map<?, ?>) map.get("foo")).containsKey("id"));
        assertTrue(((Map<?, ?>) map.get("foo")).containsKey("name"));
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("id"));
        assertEquals("test", ((Map<?, ?>) map.get("foo")).get("name"));
        assertFalse(map.containsKey("id"));
        assertFalse(map.containsKey("name"));
    }

    @Test
    void testGetPutNestedMap() {
        Map foo = new HashMap();
        foo.put("id", 1);
        foo.put("name", "test");
        NestedMap map = new NestedMap();
        map.put("foo", foo);
        assertTrue(map.containsKey("foo"));
        assertNotNull(map.get("foo"));
        assertTrue(map.get("foo") instanceof NestedMap);
        assertTrue(((Map<?, ?>) map.get("foo")).containsKey("id"));
        assertEquals(1, ((Map<?, ?>) map.get("foo")).get("id"));
        assertTrue(((Map<?, ?>) map.get("foo")).containsKey("name"));
        assertEquals("test", ((Map<?, ?>) map.get("foo")).get("name"));
        assertTrue(map.containsKey("foo.id"));
        assertTrue(map.containsKey("foo.name"));
        assertEquals(1, map.get("foo.id"));
        assertFalse(map.containsKey("id"));
        assertEquals("test", map.get("foo.name"));
        assertFalse(map.containsKey("name"));
    }

    @Test
    void testRemoveNested() {
        NestedMap map = new NestedMap();
        map.put("foo.id", 1);
        assertEquals(1, map.remove("foo.id"));
        assertFalse(map.containsKey("foo.id"));
        assertTrue(map.containsKey("foo"));
        assertTrue(map.remove("foo") instanceof NestedMap);
        assertFalse(map.containsKey("foo"));

        map = new NestedMap();
        map.put("foo.id", 1);
        assertTrue(map.remove("foo") instanceof NestedMap);
        assertFalse(map.containsKey("foo.id"));
        assertFalse(map.containsKey("foo"));
    }

    @Test
    void testSizeNested() {
        NestedMap map = new NestedMap();
        map.put("foo.id", 1);
        assertEquals(1, map.size());
        assertEquals(1, map.entrySet().size());
        assertEquals(1, ((Map<?, ?>) (map.get("foo"))).size());
        assertEquals(1, ((Map<?, ?>) (map.get("foo"))).entrySet().size());
        map.put("foo.id2", 2);
        assertEquals(1, map.size());
        assertEquals(1, map.entrySet().size());
        assertEquals(2, ((Map<?, ?>) (map.get("foo"))).size());
        assertEquals(2, ((Map<?, ?>) (map.get("foo"))).entrySet().size());
    }

    @Test
    void testGetPutArray() {
        NestedMap map = new NestedMap();
        assertNull(map.put("foo[1].id", 1));
        assertEquals(1, map.put("foo[1].id", 1));
        map.put("foo[1].name", "test");
        assertEquals(1, map.get("foo[1].id"));
        assertEquals("test", map.get("foo[1].name"));
        assertTrue(map.get("foo") instanceof List);
        assertTrue(map.get("foo[1]") instanceof Map);

        List foo = new ArrayList();
        foo.add(0, "test");
        map = new NestedMap();
        assertNull(map.put("foo", foo));
        assertEquals("test", map.get("foo[0]"));

        NestedMap source = new NestedMap();
        source.put("foo[0].id", 0);
        source.put("foo[1].id", 1);
        source.put("foo[2].id", 2);
        map = new NestedMap();
        assertNull(map.put("foo", source.get("foo*.id")));
        assertEquals(0, map.get("foo[0]"));
        assertEquals(1, map.get("foo[1]"));
        assertEquals(2, map.get("foo[2]"));

        Map bar = new HashMap();
        bar.put("id", 1);
        bar.put("name", "test");
        foo = new ArrayList();
        foo.add(0, bar);
        map = new NestedMap();
        assertNull(map.put("foo", foo));
        assertTrue(map.get("foo[0]") instanceof NestedMap);
        assertEquals(1, map.get("foo[0].id"));
        assertEquals("test", map.get("foo[0].name"));
    }

    @Test
    void testSizeArray() {
        NestedMap map = new NestedMap();
        map.put("foo[1]", 1);
        map.put("foo[0]", 2);
        assertEquals(1, map.size());
        assertEquals(1, map.entrySet().size());
        assertEquals(2, ((List<?>) map.get("foo")).size());
    }

    @Test
    void testDeepNested() {
        NestedMap map = new NestedMap();
        map.put("a.b.c[0].d[1].e", 1);
        assertEquals(1, map.size());
        assertEquals(((Map<?, ?>) map.get("a")).get("b"), map.get("a.b"));
        assertTrue(map.get("a.b.c") instanceof List);
        assertEquals(((List<?>) ((Map<?, ?>) ((Map<?, ?>) map.get("a")).get("b")).get("c")).get(0), map.get("a.b.c[0]"));
        assertTrue(map.get("a.b.c[0].d") instanceof List);
        assertEquals(((List<?>) ((Map<?, ?>) ((List<?>) ((Map<?, ?>) ((Map<?, ?>) map.get("a")).get("b")).get("c")).get(0)).get("d")).get(1), map.get("a.b.c[0].d[1]"));
        assertEquals(1, map.get("a.b.c[0].d[1].e"));
    }

    @Test
    void testContains() {
        NestedMap map = new NestedMap();
        map.put("a", 1);
        assertTrue(map.containsKey("a"));
        assertFalse(map.containsKey("a.b"));
        assertFalse(map.containsKey("a.b.c"));
        assertFalse(map.containsKey("a[0]"));
        map = new NestedMap();
        map.put("a[0]", 1);
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("a[0]"));
        assertFalse(map.containsKey("a[1]"));
        assertFalse(map.containsKey("a[0].b"));
        map.put("c.b", 1);
        assertTrue(map.containsKey("c.b"));
    }


    @Test
    void spreadNullTest() {
        NestedMap map = new NestedMap();
        map.put("foo[0].id", "0");
        map.put("foo[1].id", "1");
        Object value1 = map.get("foo*.id");
        Object value2 = map.get("foo*.name");
        assertNotNull(value1);
        assertNotNull(value2);

        //положим один name
        map.put("foo[0].name", "oleg");
        value2 = map.get("foo*.name");
        assertNotNull(value2);
        assertEquals("oleg", ((List<?>) value2).get(0));
        assertNull(((List<?>) value2).get(1));

        map = new NestedMap();
        map.put("gender.id", 1);
        assertNull(map.get("gender*.id"));

        map = new NestedMap();
        map.put("gender*.id", Arrays.asList(1, 2));
        assertNull(map.get("gender.id"));

        map = new NestedMap();
        assertNull(map.put("gender.id", 1));
        map.put("gender*.id", Arrays.asList(1, 2));

        NestedMap map1 = new NestedMap();
        assertThrows(Exception.class, () -> map1.put("gender*.id", 1));
    }

    @Test
    void testSpreadGetTest() {
        assertThrows(UnsupportedOperationException.class, () -> {
            //preparing
            NestedMap map = new NestedMap();
            map.put("foo[0].id", "0");
            map.put("foo[1].id", "1");
            map.put("foo[0].bar[0].id", "00");
            map.put("foo[0].bar[1].id", "01");
            map.put("foo[1].bar[0].id", "10");
            map.put("foo[1].bar[1].id", "11");

            //уровень вложенности spread = 1
            List fooIds = (List) map.get("foo*.id");
            assertEquals(2, fooIds.size());
            assertEquals("0", fooIds.get(0));
            assertEquals("1", fooIds.get(1));

            //уровень вложенности spread = 2
            List<List<String>> allBarIds = (List) map.get("foo*.bar*.id");
            assertEquals(2, allBarIds.size());
            assertEquals(2, allBarIds.get(0).size());
            assertEquals(2, allBarIds.get(1).size());
            assertEquals("00", allBarIds.get(0).get(0));
            assertEquals("01", allBarIds.get(0).get(1));
            assertEquals("10", allBarIds.get(1).get(0));
            assertEquals("11", allBarIds.get(1).get(1));

            List<String> barIds = (List) map.get("foo[1].bar*.id");
            assertEquals(2, barIds.size());
            assertEquals("10", barIds.get(0));
            assertEquals("11", barIds.get(1));

            //результат операции spread не модифицируем
            barIds.add("newId");
        });
    }


    @Test
    void testSpreadPutTest() {
        NestedMap map = new NestedMap();

        //вложенность spread = 1
        map.put("foo*.id", Arrays.asList(0, 1, 2));
        map.put("foo*.name", Arrays.asList("oleg", "igor"));
        assertEquals(0, map.get("foo[0].id"));
        assertEquals(1, map.get("foo[1].id"));
        assertEquals(2, map.get("foo[2].id"));
        assertEquals("oleg", map.get("foo[0].name"));
        assertEquals("igor", map.get("foo[1].name"));
        assertNull(map.get("foo[2].name"));

        //заменяем ids, проверяем возвращаемы значения
        List<Integer> returnValues = (List<Integer>) map.put("foo*.id", Arrays.asList(3, 2, 1, 0));
        assertEquals(0, returnValues.get(0));
        assertEquals(1, returnValues.get(1));
        assertEquals(2, returnValues.get(2));
        assertNull(returnValues.get(3));
        //мы заменили ids, имена не должны были пострадать
        assertEquals("oleg", map.get("foo[0].name"));
        assertEquals("igor", map.get("foo[1].name"));
        //правильно ли заменились ids
        assertEquals(3, map.get("foo[0].id"));
        assertEquals(2, map.get("foo[1].id"));
        assertEquals(1, map.get("foo[2].id"));
        assertEquals(0, map.get("foo[3].id"));

    }

    @Test
    void testSpreadPut() {
        //вложенность spread = 2
        NestedMap map = new NestedMap();
        map.put("foo*.bar*.id", Arrays.asList(Arrays.asList(0, 1, 2), Arrays.asList(0, 1)));
        assertTrue(map.get("foo") instanceof List);
        assertEquals(2, ((List<?>) map.get("foo")).size());
        assertTrue(map.get("foo[0].bar") instanceof List);
        assertEquals(3, ((List<?>) map.get("foo[0].bar")).size());
        assertTrue(map.get("foo[1].bar") instanceof List);
        assertEquals(2, ((List<?>) map.get("foo[1].bar")).size());
        assertEquals(2, map.get("foo[0].bar[2].id"));
        assertEquals(0, map.get("foo[1].bar[0].id"));

        NestedMap map1 = new NestedMap();
        map1.put("foo[0].bar*.id", Arrays.asList(0, 1));
        assertTrue(map1.get("foo") instanceof List);
        assertEquals(1, ((List<?>) map1.get("foo")).size());
        assertTrue(map1.get("foo[0].bar") instanceof List);
        assertEquals(2, ((List<?>) map1.get("foo[0].bar")).size());
        assertEquals(0, map1.get("foo[0].bar[0].id"));
        assertEquals(1, map1.get("foo[0].bar[1].id"));

        //запланированная ошибка: два оператора spread - должно быть два вложенных списка
        assertThrows(IllegalArgumentException.class, () -> map1.put("foo*.bar*.id", Arrays.asList(0, 1, 2)));

        map = new NestedMap();
        map.put("foo*.bar.id", Arrays.asList(1, 2, 3));
        assertEquals(Arrays.asList(1, 2, 3), map.get("foo*.bar.id"));
        map.put("foo*.bar.id2", Arrays.asList(1, 2, 3, 4));
        assertEquals(Arrays.asList(1, 2, 3, null), map.get("foo*.bar.id"));
        assertEquals(Arrays.asList(1, 2, 3, 4), map.get("foo*.bar.id2"));

        //test put array
        map = new NestedMap();
        map.put("test*.name", Arrays.asList("test1", "test2", "test3"));
        assertTrue(map.get("test") instanceof List);
        assertEquals(3, ((List<?>) map.get("test")).size());
    }

    @Test
    void testSpreadContainsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            NestedMap map = new NestedMap();
            map.put("foo*.id", Arrays.asList(0, 1, 2));
            map.containsKey("foo*.id");
        });
    }

    @Test
    void testSpreadPutUnmodifiable() {
        NestedMap map = new NestedMap();
        map.put("foo*.id", List.of(0, 1, 2));
        assertEquals(0, map.get("foo[0].id"));
        assertEquals(1, map.get("foo[1].id"));
        assertEquals(2, map.get("foo[2].id"));
    }


    @Test
    void testConstructor() {
        //обычная map
        Map<String, Object> baseMap = new NestedMap();
        baseMap.put("id", 124);
        baseMap.put("gender.id", 1);
        baseMap.put("gender.name", "Мужской");
        baseMap.put("individual.gender.id", 2);
        baseMap.put("individual.gender.name", "Женский");
        Map<String, Object> newMap = new NestedMap(baseMap);

        assertEquals(124, newMap.get("id"));
        assertEquals(1, newMap.get("gender.id"));
        assertEquals("Мужской", newMap.get("gender.name"));
        assertEquals(2, newMap.get("individual.gender.id"));
        assertEquals("Женский", newMap.get("individual.gender.name"));

        //nestedMap
        baseMap = new NestedMap();
        baseMap.put("id", 124);
        baseMap.put("gender.id", 1);
        baseMap.put("gender.name", "Мужской");
        baseMap.put("individual.gender.id", 2);
        baseMap.put("individual.gender.name", "Женский");
        newMap = new NestedMap(baseMap);

        assertEquals(124, newMap.get("id"));
        assertEquals(1, newMap.get("gender.id"));
        assertEquals("Мужской", newMap.get("gender.name"));
        assertEquals(2, newMap.get("individual.gender.id"));
        assertEquals("Женский", newMap.get("individual.gender.name"));
    }

    @Test
    void testMapAsList() {
        List<Object> foo = new ArrayList<>();
        Map<String, Integer> bar = new HashMap<>();
        bar.put("id", 1);

        foo.add(1);
        foo.add(bar);
        foo.add(null);
        foo.add(3);

        NestedMap map = new NestedMap();
        map.put("a", foo);

        assertTrue(map.get("a") instanceof List);
        assertEquals(4, ((List<?>) map.get("a")).size());
        assertEquals(1, map.get("a[0]"));
        assertEquals(1, map.get("a[1].id"));
        assertNull(map.get("a[2]"));
        assertEquals(3, map.get("a[3]"));

        NestedMap result = new NestedMap();
        Map<String, Object> foo2 = new HashMap<>();
        foo2.put("9", 9);
        result.put("a", foo2);
        assertEquals(9, result.get("a['9']"));
    }

    @Test
    void testBootstrapArrays() {
        NestedMap map = new NestedMap();
        //put
        map.put("list[0].id", 1);
        map.put("list[1].id", 2);
        map.put("list[1].name", "Олег");
        assertEquals(2, ((List<?>) map.get("list")).size());
        assertEquals(1, ((Map<?, ?>) ((List<?>) map.get("list")).get(0)).get("id"));
        assertEquals(2, ((Map<?, ?>) ((List<?>) map.get("list")).get(1)).get("id"));
        assertEquals("Олег", ((Map<?, ?>) ((List<?>) map.get("list")).get(1)).get("name"));
        //get
        assertEquals(1, map.get("list[0].id"));
        assertEquals(2, map.get("list[1].id"));
        assertEquals("Олег", map.get("list[1].name"));
        //contains
        assertTrue(map.containsKey("list[0].id"));
        assertTrue(map.containsKey("list[1].id"));
        assertTrue(map.containsKey("list[1].name"));
        //remove
        assertEquals(1, map.remove("list[0].id"));
        assertEquals(2, ((List<?>) map.get("list")).size());
        assertNull(((Map<?, ?>) ((List<?>) map.get("list")).get(0)).get("id"));
        assertEquals(2, ((Map<?, ?>) ((List<?>) map.get("list")).get(1)).get("id"));
        assertEquals("Олег", ((Map<?, ?>) ((List<?>) map.get("list")).get(1)).get("name"));

        //check double remove
        map.clear();
        map.put("list[0].id", 1);
        assertEquals(1, ((Map<?, ?>) map.remove("list[0]")).get("id"));
        assertEquals(0, ((List<?>) map.get("list")).size());
        assertNull(map.remove("list[0].id"));
        assertNull(map.remove("list[0]"));
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
}
