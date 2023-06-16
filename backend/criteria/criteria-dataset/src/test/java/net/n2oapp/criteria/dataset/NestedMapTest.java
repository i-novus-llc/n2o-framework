package net.n2oapp.criteria.dataset;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests of {@link NestedMap}
 */
public class NestedMapTest {

    @Test
    void get() {
        NestedMap map = new NestedMap();
        map.put("foo", 1);
        assert map.get("foo").equals(1);
        assert map.get("['foo']").equals(1);
        assert map.get("[\"foo\"]").equals(1);

        map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("bar", 1);
        map.put("foo", map2);
        assert map.get("foo.bar").equals(1);
        assert map.get("['foo'].bar").equals(1);
        assert map.get("[\"foo\"].bar").equals(1);
        assert map.get("foo['bar']").equals(1);
        assert map.get("foo[\"bar\"]").equals(1);
        assert map.get("['foo']['bar']").equals(1);
        assert map.get("[\"foo\"][\"bar\"]").equals(1);

        map = new NestedMap();
        List<Object> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("bar", 1);
        list.add(map1);
        map2 = new HashMap<>();
        map2.put("bar", 2);
        list.add(map2);
        map.put("foo", list);
        assert map.get("foo*.bar") instanceof List;
        assert map.get("['foo']*.bar") instanceof List;
        assert ((List) map.get("foo*.bar")).get(0).equals(1);
        assert ((List) map.get("foo*.bar")).get(1).equals(2);

        map = new NestedMap();
        list = new NestedList();
        list.add(1);
        map.put("foo", list);
        assert map.get("foo[0]").equals(1);

        //empty
        map = new NestedMap();
        assert map.get("bar") == null;//safe not key

        map = new NestedMap();
        map.put("foo", null);
        assert map.get("foo.bar") == null;//safe get on null

        map = new NestedMap();
        map.put("foo", 1);
        assert map.get("foo.bar") == null;//not a map

        map = new NestedMap();
        map.put("foo", 1);
        assert map.get("foo*.bar") == null;//not a list

        //negative
        assert fail(() -> new NestedMap().get(null), IllegalArgumentException.class);//key is null
    }

    @Test
    void put() {
        NestedMap map = new NestedMap();
        map.put("foo", 1);
        assert map.get("foo").equals(1);

        map = new NestedMap();
        map.put("['foo']", 1);
        assert map.get("foo").equals(1);

        map = new NestedMap();
        map.put("[\"foo\"]", 1);
        assert map.get("foo").equals(1);

        map = new NestedMap();
        map.put("foo.bar", 1);
        assert map.get("foo") instanceof Map;
        assert ((Map) map.get("foo")).get("bar").equals(1);

        map = new NestedMap();
        map.put("foo['bar']", 1);
        assert map.get("foo") instanceof Map;
        assert ((Map) map.get("foo")).get("bar").equals(1);

        map = new NestedMap();
        map.put("['foo']['bar']", 1);
        assert map.get("foo") instanceof Map;
        assert ((Map) map.get("foo")).get("bar").equals(1);

        map = new NestedMap();
        map.put("foo[2]", 1);
        assert map.get("foo") instanceof List;
        assert ((List) map.get("foo")).get(2).equals(1);

        map = new NestedMap();
        map.put("foo*.bar", Arrays.asList(1, 2, 3));
        assert map.get("foo") instanceof List;
        assert ((List) map.get("foo")).get(0) instanceof Map;
        assert ((Map) ((List) map.get("foo")).get(1)).get("bar").equals(2);

        //put map
        map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("bar", 1);
        map.put("foo", map2);
        assert ((Map)map.get("foo")).get("bar").equals(1);

        //put map 2
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("bar.test", 1);
        map.put("foo", map2);
        assert ((Map)map.get("foo")).get("['bar.test']").equals(1);

        //put map 3
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("bar['test']", 1);
        map.put("foo", map2);
        assert ((Map)map.get("foo")).get("[\"bar['test']\"]").equals(1);

        //put map 4
        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("2019-01-01", 1);
        map.put("foo", map2);
        assert ((Map)map.get("foo")).get("['2019-01-01']").equals(1);

        //negative
        map = new NestedMap();
        map.put("foo", 1);
        assert map.put("foo*.bar", null).equals(1);
        assert map.get("foo") == null;

        NestedMap map3 = new NestedMap();
        assert fail(() -> map3.put("foo*.bar", 1), IllegalArgumentException.class);//value not an iterable
    }

    @Test
    void putAll() {
        NestedMap map = new NestedMap();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("foo", 1);
        map.putAll(map2);
        assert map.get("foo").equals(1);

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("1", 1);
        map.putAll(map2);
        assert map.get("['1']").equals(1);

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo.bar", 1);
        map.putAll(map2);
        assert map.get("['foo.bar']").equals(1);
        assert map.get("foo") == null;

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo", Arrays.asList(1, 2, 3));
        map.putAll(map2);
        assert ((List)map.get("foo")).size() == 3;

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("2019-01-01", 1);
        map.putAll(map2);
        assert map.get("['2019-01-01']").equals(1);

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo[0]", 1);
        map.putAll(map2);
        assert map.get("['foo[0]']").equals(1);
        assert map.get("foo") == null;

        map = new NestedMap();
        map2 = new HashMap<>();
        map2.put("foo['bar']", 1);
        map.putAll(map2);
        assert map.get("[\"foo['bar']\"]").equals(1);
        assert map.get("foo") == null;
    }

    @Test
    void removeAndContains() {
        NestedMap map = new NestedMap();
        map.put("foo", 1);
        assert map.containsKey("foo");
        assert map.remove("foo").equals(1);
        assert !map.containsKey("foo");

        map = new NestedMap();
        map.put("foo", 1);
        assert map.containsKey("['foo']");
        assert map.remove("['foo']").equals(1);
        assert !map.containsKey("['foo']");

        map = new NestedMap();
        map.put("foo", 1);
        assert map.containsKey("[\"foo\"]");
        assert map.remove("[\"foo\"]").equals(1);
        assert !map.containsKey("[\"foo\"]");

        map = new NestedMap();
        map.put("foo.bar", 1);
        assert map.containsKey("foo.bar");
        assert map.remove("foo.bar").equals(1);
        assert !map.containsKey("foo.bar");

        map = new NestedMap();
        map.put("foo.bar", 1);
        assert map.containsKey("foo['bar']");
        assert map.remove("foo['bar']").equals(1);
        assert !map.containsKey("foo['bar']");

        map = new NestedMap();
        map.put("foo.bar", 1);
        assert map.containsKey("['foo']['bar']");
        assert map.remove("['foo']['bar']").equals(1);
        assert !map.containsKey("['foo']['bar']");

        map = new NestedMap();
        map.put("foo[2]", 1);
        assert map.containsKey("foo[2]");
        assert map.remove("foo[2]").equals(1);
        assert !map.containsKey("foo[2]");

        //cascade remove
        map = new NestedMap();
        map.put("foo.bar", 1);
        assert map.containsKey("foo");
        assert map.remove("foo") instanceof Map;
        assert !map.containsKey("foo");

        map = new NestedMap();
        map.put("foo[0]", 1);
        assert map.containsKey("foo");
        assert map.remove("foo") instanceof List;
        assert !map.containsKey("foo");

        //empty
        map = new NestedMap();
        assert !map.containsKey("foo");
        assert map.remove("foo") == null;

        map = new NestedMap();
        map.put("foo[1]", 1);
        assert !map.containsKey("foo[0]");
        assert map.remove("foo[0]") == null;

        map = new NestedMap();
        map.put("foo.bar", 1);
        assert !map.containsKey("foo[0]");
        assert map.remove("foo[0]") == null;
        assert map.containsKey("foo.bar");//not replace

        map = new NestedMap();
        map.put("foo", Arrays.asList(1, 2, 3));
        assert !map.containsKey("foo.bar");
        assert map.remove("foo.bar") == null;
        assert map.containsKey("foo[0]");//not replace

        //negative
        NestedMap map2 = new NestedMap();
        map2.put("foo.bar", 1);
        assert fail(() -> map2.containsKey("foo*.bar"), IllegalArgumentException.class);//value not an iterable
        assert fail(() -> map2.remove("foo*.bar"), IllegalArgumentException.class);//value not an iterable
        assert fail(() -> map2.containsKey(123), IllegalArgumentException.class);//value not an iterable
        assert fail(() -> map2.remove(123), IllegalArgumentException.class);//value not an iterable
    }

    @Test
    void remove() {
        NestedMap map = new NestedMap();
        map.put("id", 1);
        assert map.remove("id").equals(1);
        assert !map.containsKey("id");

        //remove not exists
        assert map.remove("a.b") == null;
    }

    //Synthetic tests

    @Test
    void size() {
        NestedMap map = new NestedMap();
        map.put("id", 1);
        assert map.size() == 1;
        map.put("id2", 2);
        assert map.size() == 2;
    }

    @Test
    void invalidPut() {
        NestedMap map = new NestedMap();
        try {
            map.put(null, 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put(".", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a.b.", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put(".a.b", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a.b..", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a[0", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a.[0]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a[b]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a['b']c", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a['b\"]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a[\"b']", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a['b]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("[a]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("['a]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("['a\"]", 1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    void invalidGet() {
        NestedMap map = new NestedMap();
        try {
            map.get(null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.get(".");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.get(".a");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map.put("a.b", 1);
            map.get("a..");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map = new NestedMap();
            map.put("a", Arrays.asList(1, 2));
            map.get("a[0");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map = new NestedMap();
            map.get("[0");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map = new NestedMap();
            map.get("[a]");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map = new NestedMap();
            map.get("a.1");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        try {
            map = new NestedMap();
            map.get("a*.1");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    /**
     * Example props
     */
    @Test
    void props() {
        NestedMap map;
        //simple props
        map = new NestedMap();
        assert map.put("a", 1) == null;
        assert map.containsKey("a");
        assert map.get("a").equals(1);
        assert map.remove("a").equals(1);
        assert !map.containsKey("a");
        assert map.get("a") == null;

        //nested props
        map = new NestedMap();
        assert map.put("a.b", 1) == null;
        assert map.containsKey("a");
        assert map.containsKey("a.b");
        assert map.get("a") instanceof NestedMap;
        assert map.get("a.b").equals(1);
        assert !map.containsKey("a.b.c");
        assert map.get("a.b.c") == null;

        assert map.remove("a.b").equals(1);
        assert !map.containsKey("a.b");
        assert map.get("a.b") == null;

        assert map.remove("a") instanceof NestedMap;
        assert !map.containsKey("a");
        assert map.get("a") == null;
    }

    /**
     * Example map array
     */
    @Test
    void mapArray() {
        NestedMap map;
        //simple map
        map = new NestedMap();
        assert map.put("['a']", 1) == null;
        assert map.containsKey("['a']");
        assert map.get("['a']").equals(1);
        assert map.remove("['a']").equals(1);
        assert !map.containsKey("['a']");
        assert map.get("['a']") == null;

        //map or props
        map = new NestedMap();
        assert map.put("['a']", 1) == null;
        assert map.containsKey("a");
        assert map.get("a").equals(1);
        assert map.remove("a").equals(1);
        assert map.put("b", 2) == null;
        assert map.containsKey("['b']");
        assert map.get("['b']").equals(2);
        assert map.remove("['b']").equals(2);

        //map or props 2
        map = new NestedMap();
        assert map.put("['a.b']", 1) == null;
        assert map.containsKey("['a.b']");
        assert map.get("['a.b']").equals(1);
        assert !map.containsKey("['a']");
        assert !map.containsKey("a");
        assert !map.containsKey("a.b");
        assert !map.containsKey("['a'].b");

        //map and props
        map = new NestedMap();
        assert map.put("a['b']", 1) == null;
        assert map.get("a") instanceof NestedMap;
        assert map.get("a['b']").equals(1);

        map = new NestedMap();
        assert map.put("a['b'].c", 1) == null;
        assert map.get("a") instanceof NestedMap;
        assert map.get("a['b']") instanceof NestedMap;
        assert map.get("a['b'].c").equals(1);

        map = new NestedMap();
        assert map.put("['a']['b'].c", 1) == null;
        assert map.get("['a']") instanceof NestedMap;
        assert map.get("['a']['b']") instanceof NestedMap;
        assert map.get("['a']['b'].c").equals(1);

        //map and props 2
        map = new NestedMap();
        assert map.put("a.b", 1) == null;
        assert map.get("a['b']").equals(1);
        assert map.get("['a']['b']").equals(1);
        assert map.get("['a'].b").equals(1);
        assert map.get("a.b").equals(1);
    }

    /**
     * Example index array
     */
    @Test
    void indexArray() {
        NestedMap map;
        //simple index
        map = new NestedMap();
        assert map.put("a[0]", 1) == null;
        assert map.put("a[1]", 2) == null;
        assert map.put("a[2]", 3) == null;
        assert map.containsKey("a");
        assert map.get("a") instanceof List;
        assert ((List) map.get("a")).size() == 3;
        assert map.containsKey("a[0]");
        assert map.get("a[0]").equals(1);
        assert map.containsKey("a[1]");
        assert map.get("a[1]").equals(2);
        assert map.containsKey("a[2]");
        assert map.get("a[2]").equals(3);
        assert !map.containsKey("a[3]");
        assert map.remove("a[1]").equals(2);//after remove array size became is 2
        assert ((List) map.get("a")).size() == 2;
        assert !map.containsKey("a[3]");

        //nested index
        map = new NestedMap();
        assert map.put("a[0][1]", 1) == null;
        assert map.containsKey("a");
        assert map.get("a") instanceof List;
        assert map.get("a[0]") instanceof List;
        assert map.get("a[1]") == null;
        assert map.get("a[0][0]") == null;
        assert map.get("a[0][1]").equals(1);

        //props and index
        map = new NestedMap();
        assert map.put("a[0].b", 1) == null;
        assert map.put("a[1].b", 2) == null;
        assert map.put("a[1].c", 3) == null;
        assert map.containsKey("a");
        assert map.get("a") instanceof List;
        assert map.get("a[0]") instanceof NestedMap;
        assert map.get("a[0].b").equals(1);
        assert map.get("a[1].b").equals(2);
        assert map.get("a[1].c").equals(3);

        //map and index
        map = new NestedMap();
        assert map.put("['a'][0]", 1) == null;
        assert map.get("['a']") instanceof List;
        assert map.get("['a'][0]").equals(1);

        //map and index and props
        map = new NestedMap();
        assert map.put("['a'][0].b", 1) == null;
        assert map.get("['a']") instanceof List;
        assert map.get("['a'][0]") instanceof NestedMap;
        assert map.get("['a'][0].b").equals(1);
    }

    @Test
    void testGetPutNested() {
        NestedMap map = new NestedMap();
        assert null == map.put("foo.id", 1);
        assert map.put("foo.id", 1).equals(1);
        assert null == map.put("foo.name", "test");
        assert map.containsKey("foo.id");
        assert map.get("foo.id").equals(1);
        assert map.containsKey("foo.name");
        assert map.get("foo.name").equals("test");
        assert map.containsKey("foo");
        assert map.get("foo") != null;
        assert map.get("foo") instanceof Map;
        assert ((Map) map.get("foo")).containsKey("id");
        assert ((Map) map.get("foo")).containsKey("name");
        assert ((Map) map.get("foo")).get("id").equals(1);
        assert ((Map) map.get("foo")).get("name").equals("test");
        assert !map.containsKey("id");
        assert !map.containsKey("name");

        Map foo = new HashMap();
        foo.put("id", 1);
        foo.put("name", "test");
        map = new NestedMap();
        map.put("foo", foo);
        assert map.containsKey("foo");
        assert map.get("foo") != null;
        assert map.get("foo") instanceof NestedMap;
        assert ((Map) map.get("foo")).containsKey("id");
        assert ((Map) map.get("foo")).get("id").equals(1);
        assert ((Map) map.get("foo")).containsKey("name");
        assert ((Map) map.get("foo")).get("name").equals("test");
        assert map.containsKey("foo.id");
        assert map.containsKey("foo.name");
        assert map.get("foo.id").equals(1);
        assert !map.containsKey("id");
        assert map.get("foo.name").equals("test");
        assert !map.containsKey("name");
    }

    @Test
    void testRemoveNested() {
        NestedMap map = new NestedMap();
        map.put("foo.id", 1);
        assert map.remove("foo.id").equals(1);
        assert !map.containsKey("foo.id");
        assert map.containsKey("foo");
        assert map.remove("foo") instanceof NestedMap;
        assert !map.containsKey("foo");

        map = new NestedMap();
        map.put("foo.id", 1);
        assert map.remove("foo") instanceof NestedMap;
        assert !map.containsKey("foo.id");
        assert !map.containsKey("foo");
    }

    @Test
    void testSizeNested() {
        NestedMap map = new NestedMap();
        map.put("foo.id", 1);
        assert map.size() == 1;
        assert map.entrySet().size() == 1;
        assert ((Map) (map.get("foo"))).size() == 1;
        assert ((Map) (map.get("foo"))).entrySet().size() == 1;
        map.put("foo.id2", 2);
        assert map.size() == 1;
        assert map.entrySet().size() == 1;
        assert ((Map) (map.get("foo"))).size() == 2;
        assert ((Map) (map.get("foo"))).entrySet().size() == 2;
    }

    @Test
    void testGetPutArray() {
        NestedMap map = new NestedMap();
        assert null == map.put("foo[1].id", 1);
        assert map.put("foo[1].id", 1).equals(1);
        map.put("foo[1].name", "test");
        assert map.get("foo[1].id").equals(1);
        assert map.get("foo[1].name").equals("test");
        assert map.get("foo") instanceof List;
        assert map.get("foo[1]") instanceof Map;

        List foo = new ArrayList();
        foo.add(0, "test");
        map = new NestedMap();
        assert null == map.put("foo", foo);
        assert map.get("foo[0]").equals("test");

        NestedMap source = new NestedMap();
        source.put("foo[0].id", 0);
        source.put("foo[1].id", 1);
        source.put("foo[2].id", 2);
        map = new NestedMap();
        assert null == map.put("foo", source.get("foo*.id"));
        assert map.get("foo[0]").equals(0);
        assert map.get("foo[1]").equals(1);
        assert map.get("foo[2]").equals(2);

        Map bar = new HashMap();
        bar.put("id", 1);
        bar.put("name", "test");
        foo = new ArrayList();
        foo.add(0, bar);
        map = new NestedMap();
        assert null == map.put("foo", foo);
        assert map.get("foo[0]") instanceof NestedMap;
        assert map.get("foo[0].id").equals(1);
        assert map.get("foo[0].name").equals("test");
    }

    @Test
    void testSizeArray() {
        NestedMap map = new NestedMap();
        map.put("foo[1]", 1);
        map.put("foo[0]", 2);
        assert map.size() == 1;
        assert map.entrySet().size() == 1;
        assert ((List) map.get("foo")).size() == 2;
    }

    @Test
    void testDeepNested() {
        NestedMap map = new NestedMap();
        map.put("a.b.c[0].d[1].e", 1);
        assert map.size() == 1;
        assert map.get("a.b").equals(((Map) map.get("a")).get("b"));
        assert map.get("a.b.c") instanceof List;
        assert map.get("a.b.c[0]").equals(((List) ((Map) ((Map) map.get("a")).get("b")).get("c")).get(0));
        assert map.get("a.b.c[0].d") instanceof List;
        assert map.get("a.b.c[0].d[1]").equals(((List) ((Map) ((List) ((Map) ((Map) map.get("a")).get("b")).get("c")).get(0)).get("d")).get(1));
        assert map.get("a.b.c[0].d[1].e").equals(1);
    }

    @Test
    void testContains() {
        NestedMap map = new NestedMap();
        map.put("a", 1);
        assert map.containsKey("a");
        assert !map.containsKey("a.b");
        assert !map.containsKey("a.b.c");
        assert !map.containsKey("a[0]");
        map = new NestedMap();
        map.put("a[0]", 1);
        assert map.containsKey("a");
        assert map.containsKey("a[0]");
        assert !map.containsKey("a[1]");
        assert !map.containsKey("a[0].b");
        map.put("c.b", 1);
        assert map.containsKey("c.b");
    }


    @Test
    void spreadNullTest() {
        NestedMap map = new NestedMap();
        map.put("foo[0].id", "0");
        map.put("foo[1].id", "1");
        Object value1 = map.get("foo*.id");
        Object value2 = map.get("foo*.name");
        assert value1 != null;
        assert value2 != null;

        //положим один name
        map.put("foo[0].name", "oleg");
        value2 = map.get("foo*.name");
        assert value2 != null;
        assert ((List) value2).get(0).equals("oleg");
        assert ((List) value2).get(1) == null;

        map = new NestedMap();
        map.put("gender.id", 1);
        assert map.get("gender*.id") == null;

        map = new NestedMap();
        map.put("gender*.id", Arrays.asList(1, 2));
        assert map.get("gender.id") == null;

        map = new NestedMap();
        assert map.put("gender.id", 1) == null;
        map.put("gender*.id", Arrays.asList(1, 2));

        map = new NestedMap();
        try {
            map.put("gender*.id", 1);
            assert false;
        } catch (Exception e) {
            assert true;
        }
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
            List<String> fooIds = (List) map.get("foo*.id");
            assert fooIds.size() == 2;
            assert fooIds.get(0).equals("0");
            assert fooIds.get(1).equals("1");

            //уровень вложенности spread = 2
            List<List<String>> allBarIds = (List) map.get("foo*.bar*.id");
            assert allBarIds.size() == 2;
            assert allBarIds.get(0).size() == 2;
            assert allBarIds.get(1).size() == 2;
            assert allBarIds.get(0).get(0).equals("00");
            assert allBarIds.get(0).get(1).equals("01");
            assert allBarIds.get(1).get(0).equals("10");
            assert allBarIds.get(1).get(1).equals("11");

            List<String> barIds = (List) map.get("foo[1].bar*.id");
            assert barIds.size() == 2;
            assert barIds.get(0).equals("10");
            assert barIds.get(1).equals("11");

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
        assert map.get("foo[0].id").equals(0);
        assert map.get("foo[1].id").equals(1);
        assert map.get("foo[2].id").equals(2);
        assert map.get("foo[0].name").equals("oleg");
        assert map.get("foo[1].name").equals("igor");
        assert map.get("foo[2].name") == null;

        //заменяем ids, проверяем возвращаемы значения
        List<Integer> returnValues = (List<Integer>) map.put("foo*.id", Arrays.asList(3, 2, 1, 0));
        assert returnValues.get(0).equals(0);
        assert returnValues.get(1).equals(1);
        assert returnValues.get(2).equals(2);
        assert returnValues.get(3) == null;
        //мы заменили ids, имена не должны были пострадать
        assert map.get("foo[0].name").equals("oleg");
        assert map.get("foo[1].name").equals("igor");
        //правильно ли заменились ids
        assert map.get("foo[0].id").equals(3);
        assert map.get("foo[1].id").equals(2);
        assert map.get("foo[2].id").equals(1);
        assert map.get("foo[3].id").equals(0);

        //вложенность spread = 2
        map = new NestedMap();
        map.put("foo*.bar*.id", Arrays.asList(Arrays.asList(0, 1, 2), Arrays.asList(0, 1)));
        assert map.get("foo") instanceof List;
        assert ((List) map.get("foo")).size() == 2;
        assert map.get("foo[0].bar") instanceof List;
        assert ((List) map.get("foo[0].bar")).size() == 3;
        assert map.get("foo[1].bar") instanceof List;
        assert ((List) map.get("foo[1].bar")).size() == 2;
        assert map.get("foo[0].bar[2].id").equals(2);
        assert map.get("foo[1].bar[0].id").equals(0);

        map = new NestedMap();
        map.put("foo[0].bar*.id", Arrays.asList(0, 1));
        assert map.get("foo") instanceof List;
        assert ((List) map.get("foo")).size() == 1;
        assert map.get("foo[0].bar") instanceof List;
        assert ((List) map.get("foo[0].bar")).size() == 2;
        assert map.get("foo[0].bar[0].id").equals(0);
        assert map.get("foo[0].bar[1].id").equals(1);

        //запланированная ошибка: два оператора spread - должно быть два вложенных списка
        try {
            map.put("foo*.bar*.id", Arrays.asList(0, 1, 2));
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }

        map = new NestedMap();
        map.put("foo*.bar.id", Arrays.asList(1, 2, 3));
        assert Arrays.asList(1, 2, 3).equals(map.get("foo*.bar.id"));
        map.put("foo*.bar.id2", Arrays.asList(1, 2, 3, 4));
        assert Arrays.asList(1, 2, 3, null).equals(map.get("foo*.bar.id"));
        assert Arrays.asList(1, 2, 3, 4).equals(map.get("foo*.bar.id2"));

        //test put array
        map = new NestedMap();
        map.put("test*.name", Arrays.asList("test1", "test2", "test3"));
        assert map.get("test") instanceof List;
        assert ((List) map.get("test")).size() == 3;
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
        map.put("foo*.id", Collections.unmodifiableList(Arrays.asList(0, 1, 2)));
        assert map.get("foo[0].id").equals(0);
        assert map.get("foo[1].id").equals(1);
        assert map.get("foo[2].id").equals(2);
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

        assert newMap.get("id").equals(124);
        assert newMap.get("gender.id").equals(1);
        assert newMap.get("gender.name").equals("Мужской");
        assert newMap.get("individual.gender.id").equals(2);
        assert newMap.get("individual.gender.name").equals("Женский");

        //nestedMap
        baseMap = new NestedMap();
        baseMap.put("id", 124);
        baseMap.put("gender.id", 1);
        baseMap.put("gender.name", "Мужской");
        baseMap.put("individual.gender.id", 2);
        baseMap.put("individual.gender.name", "Женский");
        newMap = new NestedMap(baseMap);

        assert newMap.get("id").equals(124);
        assert newMap.get("gender.id").equals(1);
        assert newMap.get("gender.name").equals("Мужской");
        assert newMap.get("individual.gender.id").equals(2);
        assert newMap.get("individual.gender.name").equals("Женский");
    }

    @Test
    @Disabled
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

        assert map.get("a") instanceof List;
        assert ((List) map.get("a")).size() == 4;
        assert map.get("a[0]").equals(1);
        assert map.get("a[1].id").equals(1);
        assert map.get("a[2]") == null;
        assert map.get("a[3]").equals(3);

        NestedMap result = new NestedMap();
        Map<String, Object> foo2 = new HashMap<>();
        foo2.put("9", 9);
        result.put("a", foo2);
        assert result.get("a['9']").equals(9);
    }

    @Test
    void testBootstrapArrays() {
        NestedMap map = new NestedMap();
        //put
        map.put("list[0].id", 1);
        map.put("list[1].id", 2);
        map.put("list[1].name", "Олег");
        assert ((List) map.get("list")).size() == 2;
        assert ((Map) ((List) map.get("list")).get(0)).get("id").equals(1);
        assert ((Map) ((List) map.get("list")).get(1)).get("id").equals(2);
        assert ((Map) ((List) map.get("list")).get(1)).get("name").equals("Олег");
        //get
        assert map.get("list[0].id").equals(1);
        assert map.get("list[1].id").equals(2);
        assert map.get("list[1].name").equals("Олег");
        //contains
        assert map.containsKey("list[0].id");
        assert map.containsKey("list[1].id");
        assert map.containsKey("list[1].name");
        //remove
        assert map.remove("list[0].id").equals(1);
        assert ((List) map.get("list")).size() == 2;
        assert ((Map) ((List) map.get("list")).get(0)).get("id") == null;
        assert ((Map) ((List) map.get("list")).get(1)).get("id").equals(2);
        assert ((Map) ((List) map.get("list")).get(1)).get("name").equals("Олег");

        //check double remove
        map.clear();
        map.put("list[0].id", 1);
        assert ((Map) map.remove("list[0]")).get("id").equals(1);
        assert ((List) map.get("list")).size() == 0;
        assert map.remove("list[0].id") == null;
        assert map.remove("list[0]") == null;
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
