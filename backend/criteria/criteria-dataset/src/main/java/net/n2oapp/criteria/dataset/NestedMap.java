package net.n2oapp.criteria.dataset;

import java.util.*;

import static net.n2oapp.criteria.dataset.NestedUtils.*;

/**
 * It's implementation of access to a {@link Map} as JavaScript objects
 * Example 1:
 * <pre>
 *    Map map = new NestedMap();
 *    map.put("foo.bar", 1);
 *    assert ((Map)map.get("foo")).get("bar").equals(1);
 * </pre>
 * <p>
 * Example 2:
 * <pre>
 *    Map map = new NestedMap();
 *    map.put("foo[0].bar", 1);
 *    assert ((Map)((List)map.get("foo")).get(0)).get("bar").equals(1);
 * </pre>
 */
public class NestedMap extends LinkedHashMap<String, Object> {

    private static final char SPREAD_OPERAND = '*';

    public NestedMap() {
    }

    public NestedMap(Map<? extends String, ?> m) {
        putAll(m);
    }


    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (String key : m.keySet()) {
            if (NestedUtils.isObjectAccess(key))
                put(key, m.get(key));
            else
                put("['" + key + "']", m.get(key));
        }
    }

    @Override
    public boolean containsKey(Object oKey) {
        if (!(oKey instanceof String))
            throw new IllegalArgumentException("Key must be String, but was " + oKey);
        String key = (String) oKey;
        KeyInfo info = getKeyInfo(key);
        Object value = super.get(info.getProperty());
        if (value == null)
            return super.containsKey(info.getProperty());
        if (!info.isNesting()) {
            return true;
        } else {
            if (!info.isSpread()) {
                if (applicableFor(value, info.getRight(), Map.class))
                    return ((Map) value).containsKey(info.getRight());//case: "foo.bar"
                if (applicableFor(value, info.getRight(), NestedList.class))
                    return ((NestedList) value).get(info.getRight()) != null;//case: "foo[0]"
                return false;
            } else {
                throw new IllegalArgumentException("Key for containsKey must not contain '*.', but was " + key);
            }
        }
    }

    @Override
    public Object get(Object oKey) {
        if (!(oKey instanceof String))
            throw new IllegalArgumentException("Key must be String, but was " + oKey);
        String key = (String) oKey;
        KeyInfo info = getKeyInfo(key);
        Object value = super.get(info.getProperty());
        if (value == null)
            return null;
        if (!info.isNesting()) {
            return value;
        } else {
            if (!info.isSpread()) {
                if (applicableFor(value, info.getRight(), Map.class))
                    return ((Map) value).get(info.getRight());//case: "foo.bar"
                if (applicableFor(value, info.getRight(), NestedList.class))
                    return ((NestedList) value).get(info.getRight());//case: "foo[0]"
                return null;
            } else {
                if (!(value instanceof List))
                    return null;//case: "foo*.bar", but "foo" isn't list
                List<Object> array = (List<Object>) value;
                List<Object> result = new ArrayList<>(array.size());
                for (Object o : array) {
                    Object child = null;
                    if (o instanceof Map)
                        child = ((Map) o).get(info.getRight());
                    result.add(child);
                }
                return Collections.unmodifiableList(result);
            }
        }
    }


    @Override
    public Object put(String key, Object value) {
        KeyInfo info = getKeyInfo(key);
        if (!info.isNesting()) {
            //case: "foo"
            return super.put(info.getProperty(), NestedUtils.wrapValue(value,
                    this::createNestedMap, this::createNestedList));
        } else {
            if (!info.isSpread()) {
                //case: "foo.bar"
                Object rightValue = super.get(info.getProperty());
                if (!applicableFor(rightValue, info.getRight())) {
                    rightValue = NestedUtils.createApplicableCollection(info.getRight(),
                            this::createNestedMap, this::createNestedList);
                    super.put(info.getProperty(), rightValue);
                }
                if (applicableFor(rightValue, info.getRight(), Map.class)) {
                    //case: "foo.bar"
                    return ((Map) rightValue).put(info.getRight(), value);
                } else {
                    //case: "foo[0]"
                    return ((NestedList) rightValue).put(info.getRight(), value);
                }
            } else {
                //case: "foo*.bar"
                List<Object> res = new ArrayList<>();
                if (value instanceof Iterable) {
                    Iterable array = (Iterable) value;
                    Object rightValue = super.get(info.getProperty());
                    if (!(rightValue instanceof NestedList)) {
                        rightValue = createNestedList(null);
                        super.put(info.getProperty(), rightValue);
                    }
                    int i = 0;
                    for (Object o : array) {
                        res.add(((NestedList)rightValue).put("[" + i + "]" + "." + info.getRight(), o));
                        i++;
                    }
                    return res;
                } else
                    throw new IllegalArgumentException("Value must be iterable or array, but was " + value);
            }
        }
    }

    @Override
    public Object remove(Object oKey) {
        if (!(oKey instanceof String))
            throw new IllegalArgumentException("Key must be String, but was " + oKey);
        String key = (String) oKey;
        KeyInfo info = getKeyInfo(key);
        if (!info.isNesting()) {
            //case: "foo"
            return super.remove(info.getProperty());
        } else {
            if (!info.isSpread()) {
                //case: "foo.bar"
                Object rightValue = super.get(info.getProperty());
                if (!applicableFor(rightValue, info.getRight())) {
                    return null;
                }
                if (applicableFor(rightValue, info.getRight(), Map.class)) {
                    //case: "foo.bar"
                    return ((Map) rightValue).remove(info.getRight());
                } else {
                    //case: "foo[0]"
                    return ((NestedList) rightValue).removeByKey(info.getRight());
                }
            } else {
                throw new IllegalArgumentException("Key for containsKey must not contain '*.', but was " + key);
            }
        }
    }

    protected NestedList createNestedList(List list) {
        if (list == null)
            return new NestedList();
        else
            return new NestedList(list);
    }

    protected NestedMap createNestedMap(Map map) {
        if (map == null)
            return new NestedMap();
        else
            return new NestedMap(map);
    }


    private static KeyInfo getKeyInfo(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key must not be null");
        if (key.isEmpty())
            throw new IllegalArgumentException("Key must not be empty");
        String left;
        String right = null;
        boolean spread = false;
        int endLeft = -1;
        if (key.startsWith("[")) {
            //case: "['a']"
            if (key.charAt(1) != '\'' && key.charAt(1) != '"') {
                throw new IllegalArgumentException("Key in brackets must be in quotes, but was " + key);
            }
            endLeft = key.indexOf(']');
            if (endLeft < 0) {
                throw new IllegalArgumentException("Key must contain ']', but was " + key);
            }
            if (key.charAt(endLeft - 1) != '\'' && key.charAt(endLeft - 1) != '"') {
                throw new IllegalArgumentException("Key in brackets must be in quotes, but was " + key);
            }
            left = key.substring(2, endLeft - 1);//['abc'] -> abc
            endLeft = endLeft + 1;
        } else {
            endLeft = getEndOfWord(key);
            if (endLeft < 0) {
                left = key;//case: "abc"
            } else {
                left = key.substring(0, endLeft);//case: "ab.cd", "ab*.cd"
            }
            if (!NestedUtils.isJavaVariable(left))
                throw new IllegalArgumentException("Key '"
                        + key + "' does not match the naming convention of java variables");
        }
        if (endLeft < 0 || key.length() <= endLeft)
            return new KeyInfo(left);
        right = key.substring(endLeft);
        if (right.startsWith("*.")) {
            spread = true;
            right = right.substring(2);
            if (!isFirstJavaVariable(right))
                throw new IllegalArgumentException("Key '"
                        + right + "' does not match the naming convention of java variables");
        } else if (right.startsWith(".")) {
            right = right.substring(1);
            if (!isFirstJavaVariable(right))
                throw new IllegalArgumentException("Key '"
                        + right + "' does not match the naming convention of java variables");
        } else if (!right.startsWith("[")) {
            throw new IllegalArgumentException("Key must start with '[', but was " + right);
        }
        return new KeyInfo(left, right, spread);
    }

    private static class KeyInfo {
        private String property;
        private String right;
        private boolean spread;

        public KeyInfo(String property) {
            this.property = property;
        }

        KeyInfo(String property, String right, boolean spread) {
            this.property = property;
            this.right = right;
            this.spread = spread;
        }

        boolean isNesting() {
            return right != null;
        }

        boolean isSpread() {
            return spread;
        }

        String getRight() {
            return right;
        }

        String getProperty() {
            return property;
        }
    }
}
