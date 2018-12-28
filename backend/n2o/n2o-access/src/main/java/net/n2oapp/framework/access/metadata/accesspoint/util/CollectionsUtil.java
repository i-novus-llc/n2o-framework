package net.n2oapp.framework.access.metadata.accesspoint.util;

import java.util.*;

public class CollectionsUtil {


    public static interface KeyExtractor<K, V> {
        public K getKey(V value);
    }

    public static interface GetNameCallback<E> {
        public String getName(E e);
    }

    public static class IdExtractor<V extends IdAware<T>, T> implements KeyExtractor<T, V> {
        @Override
        public T getKey(V value) {
            return value.getId();
        }
    }


    /**
     * применяется когда нужно "познакомить" все элементы коллекции друг с другом
     * без лишних обходов (есть тест)
     */
    public static <V extends Neighbor<V>> void introduceAllEachOther(List<V> list) {
        introduceAllEachOther(list.toArray(new Neighbor[list.size()]));
    }

    public static <V extends Neighbor<V>> void introduceAllEachOther(V[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            V v = array[i];
            for (int j = i + 1; j < length; j++) {
                V v2 = array[j];
                v.acceptNeighbor(v2);
                v2.acceptNeighbor(v);
            }
        }
    }


    public static <K, V> Map<K, V> toMap(Collection<V> list, KeyExtractor<K, V> keyExtractor) {
        Map<K, V> res = new HashMap<>();
        if (list != null) for (V v : list) {
            res.put(keyExtractor.getKey(v), v);
        }
        return res;
    }

    public static <E> String toEnumeration(Collection<E> models, GetNameCallback<E> callback) {
        return toEnumeration(models, callback, ", ");
    }

    public static <E> String toEnumeration(Collection<E> models, GetNameCallback<E> callback, String sep) {
        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        if (models != null) for (E e : models) {
            if (!begin) sb.append(sep);
            sb.append(callback.getName(e));
            begin = false;
        }
        return sb.toString();
    }


    public static List<Integer> toIntList(String string) {
        String[] split = string.split(",");
        List<Integer> res = new ArrayList<>();
        for (String s : split) {
            res.add(Integer.valueOf(s.trim()));
        }
        return res;
    }


    public static List<String> toStringList(String string) {
        String[] split = string.split(",");
        List<String> res = new ArrayList<>();
        for (String s : split) {
            res.add(s.trim());
        }
        return res;
    }

    public static Set<String> toStringSet(String string) {
        return new HashSet<>(toStringList(string));
    }


    public static <T> List<T> add(List<T> list, T t) {
        if (list == null)
            list = new ArrayList<>();
        list.add(t);
        return list;
    }

    public static <T> Set<T> add(Set<T> list, T t) {
        if (list == null)
            list = new LinkedHashSet<>();
        list.add(t);
        return list;
    }

    public static <T> List<T> checkForNull(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }


    public static <T> Set<T> checkForNull(Set<T> list) {
        return list != null ? list : Collections.emptySet();
    }

    public static String listToString(String split, List<String> args) {
        if(args == null || args.size() == 0){
            return null;
        }
        StringBuilder result = new StringBuilder(args.get(0));
        for (int i = 1; i< args.size(); i++) {
            result.append(split).append(args.get(i));
        }
        return result.toString();
    }


}
