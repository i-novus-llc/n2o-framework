package net.n2oapp.criteria.api;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iryabov
 * Date: 13.02.13
 * Time: 14:45
 */
public class CriteriaAccessor {
    private static CriteriaAccessor ourInstance = new CriteriaAccessor();

    public static CriteriaAccessor getInstance() {
        return ourInstance;
    }

    private Map<Class, Map<String, Method>> criteriaReadMethods =
            new ConcurrentHashMap<Class, Map<String, Method>>();
    private Map<Class, Map<String, Method>> criteriaWriteMethods =
            new ConcurrentHashMap<Class, Map<String, Method>>();

    @SuppressWarnings("unchecked")
    public <T> T read(Criteria criteria, String field) {
        Method method = getReadMethod(criteria.getClass(), field);
        if (method != null) {
            try {
                return (T) method.invoke(criteria);
            } catch (Exception e) {
                throw new RuntimeException("Read access error: criteria " + criteria.getClass() + ", field " + field, e);
            }
        }
        if (criteria instanceof Map) {
            return ((Map<String, T>) criteria).get(field);
        }
        throw new IllegalStateException("Field not found: criteria " + criteria.getClass() + ", field " + field);
    }


    public void write(Criteria criteria, String field, Object value) {
        Method method = getWriteMethod(criteria.getClass(), field);
        write(criteria, field, value, method);
    }

    @SuppressWarnings("unchecked")
    public void write(Criteria criteria, String field, Object value, Method method) {
        if (method != null) {
            try {
                method.invoke(criteria, value);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Write access error: criteria " + criteria.getClass() + ", field " + field, e);
            }
        }
        if (criteria instanceof Map) {
            ((Map<String, Object>) criteria).put(field, value);
            return;
        }
        throw new IllegalStateException("Field not found: criteria " + criteria.getClass() + ", field " + field);
    }


    private Method getReadMethod(Class criteriaClass, String field) {
        if (!criteriaReadMethods.containsKey(criteriaClass)) {
            init(criteriaClass);
        }
        return criteriaReadMethods.get(criteriaClass).get(toCamelCase(field));
    }

    public Method getWriteMethod(Class criteriaClass, String field) {
        if (!criteriaWriteMethods.containsKey(criteriaClass)) {
            init(criteriaClass);
        }
        return criteriaWriteMethods.get(criteriaClass).get(toCamelCase(field));
    }

    private synchronized void init(Class criteriaClass) {
        if (criteriaWriteMethods.containsKey(criteriaClass)) return;
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(criteriaClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        HashMap<String, Method> readMap = new HashMap<String, Method>();
        HashMap<String, Method> writeMap = new HashMap<String, Method>();
        for (PropertyDescriptor pd : pds) {
            readMap.put(pd.getName(), pd.getReadMethod());
            writeMap.put(pd.getName(), pd.getWriteMethod());
        }
        criteriaReadMethods.put(criteriaClass, readMap);
        criteriaWriteMethods.put(criteriaClass, writeMap);
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        String[] words = init.split("\\.");
        for (int k = 0; k < words.length; k++) {
            if (k == 0) {
                ret.append(words[k]);
                continue;
            }
            if (!words[k].isEmpty()) {
                ret.append(words[k].substring(0, 1).toUpperCase());
                ret.append(words[k].substring(1).toLowerCase());
            }
        }

        return ret.toString();
    }

    public boolean contains(Criteria criteria, String field) {
        Method method = getReadMethod(criteria.getClass(), field);
        if (method != null) {
            return true;
        }
        if (criteria instanceof Map) {
            return ((Map) criteria).containsKey(field);
        }
        throw new IllegalStateException("Field not found: criteria " + criteria.getClass() + ", field " + field);
    }
}
