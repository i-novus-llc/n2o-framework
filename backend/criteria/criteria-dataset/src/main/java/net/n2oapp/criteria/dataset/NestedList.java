package net.n2oapp.criteria.dataset;

import java.util.*;

import static net.n2oapp.criteria.dataset.NestedUtils.applicableFor;

/**
 * It's implementation of access to a {@link ArrayList} as JavaScript objects
 * Example 1:
 * <pre>
 *    NestedList list = new NestedList();
 *    list.put("[0].foo", 1);
 *    assert ((Map)list.get(0)).get("foo").equals(1);
 * </pre>
 * <p>
 */
public class NestedList extends ArrayList<Object> {

    public NestedList() {
        super();
    }

    public NestedList(Collection<?> c) {
        super(c.size());
        int i = 0;
        for (Object o : c) {
            put("[" + i + "]", o);
            i++;
        }
    }

    public Object get(String key) {
        KeyInfo info = getKeyInfo(key);
        if (size() <= info.getIndex())
            return null;//case: "[12]", but size 10
        Object value = super.get(info.getIndex());
        if (value == null)
            return null;
        if (!info.isNesting()) {
            return value;
        } else {
            if (!info.isSpread()) {
                if (applicableFor(value, info.getRight(), Map.class)) {
                    return ((Map) value).get(info.getRight());//case: "[0].foo"
                }
                if (applicableFor(value, info.getRight(), NestedList.class))
                    return ((NestedList) value).get(info.getRight());//case: "[0][1]"
                return null;
            } else {
                if (!(value instanceof List))
                    return null;//case: "[0]*.foo", but "[0]" isn't list
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

    public Object put(String key, Object value) {
        KeyInfo info = getKeyInfo(key);
        if (!info.isNesting()) {
            //case: "[0]"
            NestedUtils.fillArray(this, info.getIndex());
            return super.set(info.getIndex(), NestedUtils.wrapValue(value,
                    this::createNestedMap, this::createNestedList));
        } else {
            if (!info.isSpread()) {
                //case: "[0].foo"
                NestedUtils.fillArray(this, info.getIndex());
                Object rightValue = super.get(info.getIndex());
                if (!applicableFor(rightValue, info.getRight())) {
                    rightValue = NestedUtils.createApplicableCollection(info.getRight(),
                            this::createNestedMap, this::createNestedList);
                    super.set(info.getIndex(), rightValue);
                }
                if (applicableFor(rightValue, info.getRight(), NestedMap.class)) {
                    //case: "[0].foo"
                    return ((Map) rightValue).put(info.getRight(), value);
                } else {
                    //case: "[0][1]"
                    return ((NestedList) rightValue).put(info.getRight(), value);
                }
            } else {
                //case: "[0]*.foo"
                if (value == null) {
                    return super.set(info.getIndex(), null);
                } else if (value instanceof Iterable) {
                    List<Object> res = new ArrayList<>();
                    Iterable array = (Iterable) value;
                    NestedUtils.fillArray(this, info.getIndex());
                    Object rightValue = super.get(info.getIndex());
                    if (!(rightValue instanceof NestedList)) {
                        rightValue = createNestedList(null);
                        super.set(info.getIndex(), rightValue);
                    }
                    int i = 0;
                    for (Object o : array) {
                        res.add(((NestedList) rightValue).put("[" + i + "]" + "." + info.getRight(), o));
                        i++;
                    }
                    return res;
                } else
                    throw new IllegalArgumentException("Value must be iterable or array, but was " + value);
            }
        }
    }

    public Object removeByKey(Object oKey) {
        if (!(oKey instanceof String))
            throw new IllegalArgumentException("Argument must be String, but was " + oKey);
        String key = (String) oKey;
        KeyInfo info = getKeyInfo(key);
        if (!info.isNesting()) {
            //case: "foo"
            if (size() > info.getIndex())
                return super.remove(info.getIndex());
            else
                return null;
        } else {
            if (!info.isSpread()) {
                //case: "foo.bar"
                Object rightValue = size() > info.getIndex() ? super.get(info.getIndex()) : null;
                if (!applicableFor(rightValue, info.getRight())) {
                    return null;
                }
                if (applicableFor(rightValue, info.getRight(), NestedMap.class)) {
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

    protected NestedMap createNestedMap(Map map) {
        if (map != null)
            return new NestedMap(map);
        else
            return new NestedMap();
    }

    protected NestedList createNestedList(List list) {
        if (list != null)
            return new NestedList(list);
        else
            return new NestedList();
    }


    private static KeyInfo getKeyInfo(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key must not be null");
        if (key.isEmpty())
            throw new IllegalArgumentException("Key must not be empty");
        if (!key.startsWith("["))
            throw new IllegalArgumentException("Key must start with '[', but was " + key);

        String left;
        String right;
        boolean spread = false;
        int endBracket = key.indexOf(']');
        if (endBracket < 0)
            throw new IllegalArgumentException("Key must contain ']', but was " + key);
        left = key.substring(0, endBracket + 1);
        String property = left.substring(1, left.length() - 1);
        if (!NestedUtils.isNumeric(property))
            throw new IllegalArgumentException("Key in bracket must be number, but was " + key);
        int index = Integer.parseInt(property);
        if (endBracket == key.length() - 1) {
            return new KeyInfo(index);
        } else {
            right = key.substring(endBracket + 1);
            if (right.startsWith("*.")) {
                spread = true;
                right = right.substring(1);//case: "[0]*.foo"
            }
            if (right.startsWith(".")) {
                right = right.substring(1);//case: "[0].foo"
                char ch = right.charAt(0);
                if (!Character.isJavaIdentifierStart(ch))
                    throw new IllegalArgumentException("Key '"
                            + right + "' must match the naming convention of java variables, but was " + key);
            } else if (!right.startsWith("["))
                throw new IllegalArgumentException("Key after ']' must contain '.' or '*.' or '['");

            return new KeyInfo(index, right, spread);
        }
    }

    private static class KeyInfo {


        private int index;
        private String right;
        private boolean spread;

        KeyInfo(int index) {
            this.index = index;
        }

        KeyInfo(int index, String right, boolean spread) {
            this.index = index;
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

        int getIndex() {
            return index;
        }
    }

}