package net.n2oapp.criteria.dataset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class NestedUtils {

    public static boolean isNumeric(String key) {
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    public static boolean isJavaVariable(String key) {
        if (!Character.isJavaIdentifierStart(key.charAt(0)))
            return false;
        for (char c : key.toCharArray()) {
            if (!Character.isJavaIdentifierPart(c))
                return false;
        }
        return true;
    }

    public static boolean isFirstJavaVariable(String key) {
        int endOfWord = getEndOfWord(key);
        String word;
        if (endOfWord < 0)
            word = key;
        else
            word = key.substring(0, endOfWord);
        if (word.isEmpty())
            return false;
        return isJavaVariable(word);
    }

    public static int getEndOfWord(String key) {
        int endOfWord = -1;
        int dotIdx = key.indexOf('.');
        int spreadIdx = key.indexOf("*.");
        endOfWord = calculateEndOfWord(dotIdx, spreadIdx);
        int bracketIdx = key.indexOf('[');
        endOfWord = calculateEndOfWord(endOfWord, bracketIdx);
        return endOfWord;
    }

    private static int calculateEndOfWord(int firstIdx, int secondIdx) {
        if (firstIdx < 0 && secondIdx < 0) {
            return -1;
        } else if (firstIdx > 0 && secondIdx < 0) {
            return firstIdx;
        } else if (firstIdx < 0 && secondIdx > 0) {
            return secondIdx;
        }
        return Math.min(firstIdx, secondIdx);
    }

    public static void fillArray(List<Object> list, int idx) {
        for (int k = list.size(); k <= idx; k++) {
            list.add(null);
        }
    }

    @SuppressWarnings("unchecked")
    public static Object wrapValue(Object value,
                                   Function<? super Map, ? extends NestedMap> mapConstructor,
                                   Function<? super List, ? extends NestedList> listConstructor) {
        if (value instanceof NestedMap || value instanceof NestedList)
            return value;
        if (value instanceof Map map) {
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) map).entrySet()) {
                Object entryWrap = wrapValue(entry.getValue(), mapConstructor, listConstructor);
                if (entryWrap != entry.getValue())
                    entry.setValue(entryWrap);
            }
            return mapConstructor.apply(map);
        } else if (value instanceof List list) {
            List result = listConstructor.apply(list);
            for (int k = 0; k < list.size(); k++) {
                result.set(k, wrapValue(list.get(k), mapConstructor, listConstructor));
            }
            value = result;
        }
        return value;
    }

    public static String encodeKey(String key) {
        return key.replace("'", "@27").replace("\"", "@22");
    }

    public static String decodeKey(String key) {
        return key.replace("@27", "'").replace("@22", "\"");
    }

    public static String wrapKey(String key) {
        if (key == null || key.isEmpty())
            return key;
        return "['" + encodeKey(key) + "']";
    }

    public static boolean applicableFor(Object value, String key) {
        return value != null && ((Map.class.isAssignableFrom(value.getClass())
                && NestedUtils.getAccessClass(key).equals(NestedMap.class)) ||
                (NestedList.class.isAssignableFrom(value.getClass())
                        && NestedUtils.getAccessClass(key).equals(NestedList.class)));
    }

    public static boolean applicableFor(Object value, String key, Class clazz) {
        return value != null && (clazz.isAssignableFrom(value.getClass())
                && clazz.isAssignableFrom(getAccessClass(key)));
    }

    public static Class<?> getAccessClass(String key) {
        if (key.startsWith("[") && !key.startsWith("['") && !key.startsWith("[\"")) {
            return NestedList.class;
        } else {
            return NestedMap.class;
        }
    }

    public static Object createApplicableCollection(String key,
                                                    Function<? super Map, ? extends NestedMap> mapConstructor,
                                                    Function<? super List, ? extends NestedList> listConstructor) {
        if (getAccessClass(key).equals(NestedList.class)) {
            return listConstructor.apply(null);
        } else {
            return mapConstructor.apply(null);
        }
    }
}
