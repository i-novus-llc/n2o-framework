package net.n2oapp.criteria.dataset;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

public class NestedListTest {

    @Test
    void get() {
        NestedList list = new NestedList();
        list.add(1);
        assert list.get("[0]").equals(1);

        list = new NestedList();
        Map<String, Object> map = new HashMap<>();
        map.put("foo", 1);
        list.add(map);
        assert list.get("[0].foo").equals(1);

        list = new NestedList();
        List<Object> nested = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("foo", 1);
        nested.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("foo", 2);
        nested.add(map2);
        list.add(nested);
        assert list.get("[0]*.foo") instanceof List;
        assert ((List) list.get("[0]*.foo")).get(0).equals(1);
        assert ((List) list.get("[0]*.foo")).get(1).equals(2);

        list = new NestedList();
        nested = new NestedList();
        nested.add(1);
        list.add(nested);
        assert list.get("[0][0]").equals(1);

        //empty
        list = new NestedList();
        list.add(1);
        assert list.get("[1]") == null;//safe index of bound

        list = new NestedList();
        list.add(null);
        assert list.get("[0].foo") == null;//safe get on null

        list = new NestedList();
        list.add(1);
        assert list.get("[0].foo") == null;//not a map

        list = new NestedList();
        list.add(1);
        assert list.get("[0]*.foo") == null;//not a list
    }

    @Test
    void put() {
        NestedList list = new NestedList();
        list.put("[1]", 1);
        assert list.get(1).equals(1);

        list = new NestedList();
        list.put("[1].foo", 1);
        assert list.get(1) instanceof Map;
        assert ((Map) list.get(1)).get("foo").equals(1);

        list = new NestedList();
        list.put("[1][2]", 1);
        assert list.get(1) instanceof List;
        assert ((List) list.get(1)).get(2).equals(1);

        list = new NestedList();
        list.put("[1]*.foo", Arrays.asList(1, 2, 3));
        assert list.get(1) instanceof List;
        assert ((List) list.get(1)).get(0) instanceof Map;
        assert ((Map) ((List) list.get(1)).get(0)).get("foo").equals(1);

        //negative
        list = new NestedList();
        list.put("[0]", 1);
        assert list.put("[0]*.foo", null).equals(1);
        assert list.get(0) == null;

        NestedList list2 = new NestedList();
        assert fail(() -> list2.put("[1]*.foo", 1), IllegalArgumentException.class);//value not an iterable
    }

    @Test
    void create() {
        NestedList list = new NestedList(Arrays.asList(
                1,
                Arrays.asList(1, 2),
                Collections.singletonMap("foo", 1)));
        assert list.size() == 3;
        assert list.get(0).equals(1);
        assert list.get(1) instanceof NestedList;
        assert list.get(2) instanceof NestedMap;
    }

    @Test
    void negative() {
        NestedList list = new NestedList();
        assert fail(() -> list.put(null, 1), IllegalArgumentException.class);//null key
        assert fail(() -> list.put("", 1), IllegalArgumentException.class);//empty key
        assert fail(() -> list.put("0", 1), IllegalArgumentException.class);//there isn't a '['
        assert fail(() -> list.put("[0", 1), IllegalArgumentException.class);//there isn't a ']'
        assert fail(() -> list.put("[foo]", 1), IllegalArgumentException.class);//not a numeric
        assert fail(() -> list.put("[0].123", 1), IllegalArgumentException.class);//not a java var
        assert fail(() -> list.put("[0]*.123", 1), IllegalArgumentException.class);//not a java var in spread
        assert fail(() -> list.put("[0]123", 1), IllegalArgumentException.class);//there isn't a '.'
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
