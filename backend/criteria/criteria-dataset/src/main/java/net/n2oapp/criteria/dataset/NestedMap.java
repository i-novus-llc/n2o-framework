package net.n2oapp.criteria.dataset;

import java.util.*;

/**
 * It's implementation of access to a {@link Map} as JavaScript objects
 * Example 1:
 * <pre>
 *    Map map = new NestedMap();
 *    map.put("foo.bar", 1);
 *    assert ((Map)map.get("foo")).get("bar").equals(1);
 * </pre>
 *
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
            put(key, m.get(key));
        }
    }

    @Override
    public boolean containsKey(Object oKey) {
        String key = (String) oKey;
        if (key.indexOf(SPREAD_OPERAND) > 0)
            throw new IllegalArgumentException("Method contains unsupport spread operand");
        KeyInfo keyInfo = getKeyInfo(key);
        int arrayNum = keyInfo.getArrayElementNum();
        String property = keyInfo.getLeft();
        if (keyInfo.isNesting()) {
            //case: "a.b"
            Map map = null;
            Object val = super.get(property);
            if ((arrayNum >= 0) && (val instanceof List)) {
                //case: "a[0]"
                List array = (List) val;
                Object arrVal = array.get(arrayNum);
                if (arrVal instanceof Map)
                    map = (Map) array.get(arrayNum);
            } else if (val instanceof Map) {
                //case: "a.b"
                map = (Map) val;
            }
            return map != null && map.containsKey(keyInfo.getRight());
        } else {
            //case: "ab", "a[0]"
            Object val = super.get(property);
            if (arrayNum >= 0) {
                //case: "a[0]"
                if (!(val instanceof List))
                    return false;
                List array = (List) val;
                return (array.size() > arrayNum) && (array.get(arrayNum) != null);
            } else {
                //case: "ab"
                return super.containsKey(property);
            }
        }
    }

    @Override
    public Object get(Object key) {
        KeyInfo keyInfo = getKeyInfo(key);
        int arrayNum = keyInfo.getArrayElementNum();
        String property = keyInfo.getLeft();
        boolean nesting = keyInfo.isNesting();
        if (nesting) {
            Map map;
            if (keyInfo.isSpread()) {
                Object tmp = super.get(property);
                if (tmp == null || !(tmp instanceof List)) return null;
                List array = (List) tmp;
                List res = new ArrayList(array.size());
                for (Object o : array) {
                    res.add(((Map) o).get(keyInfo.getRight()));
                }
                boolean isNull = true;
                for (Object re : res) {
                    if (re != null) isNull = false;
                }
                if (isNull) return null;
                return Collections.unmodifiableList(res);
            } else if (arrayNum >= 0) {
                List array = (List) super.get(property);
                map = array != null ? (Map) array.get(arrayNum) : null;
            } else {
                Object tmp = super.get(property);
                if (!(tmp instanceof Map)) return null;
                map = (Map) tmp;
            }
            return map != null ? map.get(keyInfo.getRight()) : null;
        } else {
            if (arrayNum >= 0) {
                List array = (List) super.get(property);
                return array != null ? array.get(arrayNum) : null;
            } else {
                return super.get(property);
            }
        }
    }


    @Override
    public Object put(String key, Object value) {
        KeyInfo keyInfo = getKeyInfo(key);
        int arrayNum = keyInfo.getArrayElementNum();
        String property = keyInfo.getLeft();
        Object wrappedValue = null;
        if (!keyInfo.isSpread())
            wrappedValue = wrapValue(value);
        if (keyInfo.isNesting()) {
            //case: "a.b.c"
            String right = keyInfo.getRight();

            Object returnValue;
            if (keyInfo.isSpread()) {
                //case: "a*.b"
                List<Object> res = new ArrayList<>();
                if (value == null)
                    return null;
                if (value instanceof Iterable) {
                    //case: "a.b" : [0,1,2,3] as List
                    Iterable array = (Iterable) value;
                    int i = 0;
                    for (Object o : array) {
                        res.add(this.put(property + '[' + i++ + ']' + '.' + right, o));
                    }
                } else if (value.getClass().isArray()) {
                    //case: "a.b" : [0,1,2,3]
                    Object[] array = (Object[]) value;
                    int i = 0;
                    for (Object o : array) {
                        res.add(this.put(property + '[' + i++ + ']' + '.' + right, o));
                    }
                } else
                    throw new IllegalArgumentException("Value for " + key + " must be iterable or array");
                returnValue = res;

            } else if (keyInfo.hasArrayElement()) {
                //case: "a[0].b"
                Object tmp = super.get(property);
                if (tmp == null || !(tmp instanceof List)) {
                    tmp = new ArrayList();
                }
                List array = (List) tmp;

                fillArray(array, arrayNum);

                Map<String, Object> map = array.size() > arrayNum ? (Map<String, Object>) array.get(arrayNum) : null;
                if (map == null) {
                    map = createNestedMap(right);
                }
                returnValue = map.put(right, wrappedValue);
                array.set(arrayNum, map);
                super.put(property, array);
            } else {
                //case: "a.b", "['a'].b", "a['b']"
                Map<String, Object> map = (Map<String, Object>) super.get(property);
                if (map == null) {
                    map = createNestedMap(right);
                }
                returnValue = map.put(right, wrappedValue);
                super.put(property, map);
            }
            return returnValue;
        } else {
            if (keyInfo.hasArrayElement()) {
                //case: "a[1]", "['a'][1]"
                List array = (List) super.get(property);
                if (array == null) {
                    array = new ArrayList();
                }
                fillArray(array, arrayNum);
                Object returnValue = array.size() > arrayNum ? array.get(arrayNum) : null;
                array.set(arrayNum, wrappedValue);
                super.put(property, array);
                return returnValue;
            } else {
                //case: "ab"
                return super.put(property, wrappedValue);
            }
        }
    }

    @Override
    public Object remove(Object oKey) {
        String key = (String) oKey;
        KeyInfo keyInfo = getKeyInfo(key);
        int arrayNum = keyInfo.getArrayElementNum();
        String property = keyInfo.getLeft();
        boolean nesting = keyInfo.isNesting();
        if (nesting) {
            Map map;
            if (arrayNum >= 0) {
                List array = (List) super.get(property);
                map = array != null && array.size() > arrayNum ? (Map) array.get(arrayNum) : null;
            } else {
                map = (Map) super.get(property);
            }
            if (map != null) {
                return map.remove(keyInfo.getRight());
            } else {
                return null;
            }
        } else {
            if (arrayNum >= 0) {
                List array = (List) super.get(property);
                return array.size() > arrayNum ? array.remove(arrayNum) : null;
            } else {
                return super.remove(property);
            }
        }
    }


    @SuppressWarnings("unchecked")
    protected Object wrapValue(Object value) {
        if (value instanceof NestedMap)
            return new NestedMap((NestedMap) value);
        if (value instanceof Map) {
            Map map = (Map) value;

            if (!map.keySet().isEmpty()) {
                Object firstKey = map.keySet().iterator().next();
                if (isNumeric(firstKey)) {
                    //если в мапе все ключи - числа, то превращаем мапу в список
                    int max = getMaxNumberInMap(map);
                    //инициализация списка пустыми значениями
                    List list = new ArrayList<>(max + 1);
                    fillArray(list, max);
                    //заполнение списка значениями из мапы
                    for (Object key : map.keySet()) {
                        int idx = Integer.parseInt(String.valueOf(key));
                        list.set(idx, wrapValue(map.get(key)));
                    }
                    return list;
                }
            }

            //оборачиваем значения мапы
            for (Object key : map.keySet()) {
                Object entryValue = map.get(key);
                Object entryWrap = wrapValue(map.get(key));
                if (entryWrap != entryValue)
                    map.put(key, entryWrap);
            }
            return createNestedMap(map);
        } else if (value instanceof List) {
            //оборачиваем значения списка
            List list = ((List) value);
            List result = new ArrayList<>(list);
            for (int k = 0; k < list.size(); k++) {
                result.set(k, wrapValue(list.get(k)));
            }
            value = result;
        }
        return value;
    }

    /**
     * @param map - мапа
     * @return находит в мапе максимальный ключ
     */
    private int getMaxNumberInMap(Map map) {
        Integer max = null;
        for (Object key : map.keySet()) {
            if (isNumeric(key)) {
                Integer candidate = Integer.parseInt(String.valueOf(key));
                if (max != null) {
                    if (candidate.compareTo(max) > 0) {
                        max = candidate;
                    }
                } else {
                    max = candidate;
                }
            }
        }
        return max;
    }

    private void fillArray(List list, int idx) {
        for (int k = list.size(); k <= idx; k++) {
            list.add(null);
        }
    }

    protected NestedMap createNestedMap(String key) {
        return new NestedMap();
    }

    protected NestedMap createNestedMap(Map map) {
        return new NestedMap(map);
    }

    private static boolean isNumeric(Object key) {
        if (key instanceof String) {
            if (((String) key).isEmpty())
                return false;
            String str = (String) key;
            char c = str.charAt(0);
            return (c >= '0' && c <= '9');
        } else
            return key instanceof Number;
    }


    private static KeyInfo getKeyInfo(Object oKey) {
        if (oKey == null)
            throw new IllegalArgumentException("Key must not be null");
        String key = (String) oKey;
        if (key.isEmpty())
            throw new IllegalArgumentException("Key must not be empty");
        char ch = key.charAt(0);
        if (!key.startsWith("['") && !key.startsWith("[\"") && !Character.isJavaIdentifierStart(ch))
            throw new IllegalArgumentException("Key '"
                    + key + "' does not match the naming convention of java variables");
        String left;
        String right;
        int dotIdx = key.indexOf('.');//case: "a.b"
        int nestingIdx = dotIdx;
        if (dotIdx >= 0 && (dotIdx == key.length() - 1))
            throw new IllegalArgumentException("Dot ('.') must not be placed at the end of the key '" + key + "'");

        if (dotIdx > 0) {
            //case: "a.b"
            left = key.substring(0, dotIdx); //case: "a.b", left: "a"
            right = key.substring(dotIdx + 1); //case: "a.b", right: "b"
        } else {
            //case: "ab"
            left = key;//case: "ab", left: "ab"
            right = "";
        }

        Integer betweenDotsInt = null;
        int secondDotIdx = -1;
        if (dotIdx > 0) {
            //case: "a.1.b"
            secondDotIdx = key.indexOf('.', dotIdx + 1);
            String betweenDots;
            if (secondDotIdx > dotIdx) {
                //case: "a.1.c"
                betweenDots = key.substring(dotIdx + 1, secondDotIdx); //case: "a.1.c", betweenDots: "1"
            } else {
                //case: "a.1"
                betweenDots = key.substring(dotIdx + 1); //case: "a.1", betweenDots: "1"
            }
            if (isNumeric(betweenDots)) {
                betweenDotsInt = Integer.parseInt(betweenDots);//case: "a.1.b", betweenDotsInt: 1
            }
        }


        int arrayElementIdx = left.indexOf('[');
        int spreadIdx = left.indexOf(SPREAD_OPERAND);
        int arrayElementNum = -1;

        if (betweenDotsInt != null) {
            //case: "a.1.b"
            arrayElementIdx = dotIdx;
            arrayElementNum = betweenDotsInt;
            left = left.substring(0, arrayElementIdx);
            if (secondDotIdx >= 0) {
                //case: "a.1.b"
                right = key.substring(secondDotIdx + 1);//case: "a.1.b", right: "b"
                nestingIdx = secondDotIdx;
            } else {
                //case: "a.1"
                right = "";
                nestingIdx = -1;
            }
        } else if (arrayElementIdx > spreadIdx) {
            //case: "a[1].b", "a['b'].c", "['ab'].c"
            String index = key.substring(arrayElementIdx + 1, key.indexOf(']'));
            if (isNumeric(index)) {
                //case: "a[1]"
                arrayElementNum = Integer.parseInt(index);//case: "a[1]", arrayElementNum: 1
                left = key.substring(0, arrayElementIdx);
                right = key.substring(key.indexOf("]") + 1);
                right = getRight(key, right);
                //todo case: "[1]"
            } else if ((index.startsWith("'") || index.startsWith("\""))
                    && (index.endsWith("'") || index.endsWith("\""))) {
                //case: "a['b'].c", "['a'].b", "['a']['b'].c"
                //todo case: "['a'][0]"
                if (arrayElementIdx > 0) {
                    //case: "a['b'].c"
                    left = key.substring(0, arrayElementIdx);//case: "a['b']", left: "a"
                    right = key.substring(arrayElementIdx);
                } else {
                    //case: "['a'].b"
                    left = index.substring(1, index.length() - 1);//case: "['a'].b", left: "a"
                    right = key.substring(key.indexOf("]") + 1);
                    right = getRight(key, right);
                }
                arrayElementIdx = -1;
                arrayElementNum = -1;
            } else
                throw new IllegalArgumentException("Index of array must be a number or a quoted string, but it was '" + index + "'");
        } else if (spreadIdx > 0) {
            //case: "a*.b"
            left = left.substring(0, spreadIdx);//case: "a*.b", left: "a"
            right = key.substring(dotIdx + 1);//case: "a*.b", right: "b"
        }
        return new KeyInfo(nestingIdx, arrayElementIdx, arrayElementNum, spreadIdx, left, right);
    }

    private static String getRight(String key, String right) {
        if (!right.isEmpty()) {
            if (right.startsWith(".")) {
                //case: "a[1].c", right: "c"
                right = right.substring(1);
            } else if (right.startsWith("[")) {
                //case: "a[1]['b']", right: "['b']"
                right = right;
            } else {
                //case "a[1]c"
                throw new IllegalArgumentException("After square bracket ']' must be dot '].' or new square bracket '][', but was " + key + "");
            }
        }
        return right;
    }

    private static class KeyInfo {
        private int dotIdx = -1;
        private int arrayElementIdx = -1;
        private int arrayElementNum = -1;
        private int spreadIdx = -1;
        private String left;
        private String right;

        KeyInfo(int dotIdx, int arrayElementIdx, int arrayElementNum, int spreadIdx,
                String left, String right) {
            this.dotIdx = dotIdx;
            this.arrayElementIdx = arrayElementIdx;
            this.arrayElementNum = arrayElementNum;
            this.spreadIdx = spreadIdx;
            this.left = left;
            this.right = right;
        }

        boolean isNesting() {
            return right.length() > 0;
        }

        boolean hasArrayElement() {
            return arrayElementIdx > 0;
        }

        boolean isSpread() {
            return spreadIdx > 0;
        }

        int getArrayElementNum() {
            return arrayElementNum;
        }

        String getRight() {
            return right;
        }

        String getLeft() {
            return left;
        }
    }


}
