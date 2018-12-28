package net.n2oapp.framework.api.metadata.local.util;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author V. Alexeev.
 */
@Deprecated
public class CompileQueryUtil {


    public static Map<String, String> initDisplayValues(List<N2oQuery.Field> displayFields) {
        Map<String, String> displayValues = new HashMap<>();
        for (N2oQuery.Field field : displayFields) {
            if (field.getSelectDefaultValue() != null) {
                displayValues.put(field.getId(), checkForNull(field.getSelectDefaultValue()));
            }
        }
        return displayValues;
    }

    private static String checkForNull(String value) {
        if ("null".equals(value))
            value = null;
        return value;
    }

    public static List<N2oQuery.Field> initSortingFields(List<N2oQuery.Field> fields) {
        List<N2oQuery.Field> result = new ArrayList<>();
        for (N2oQuery.Field field : fields) {
            if (!field.getNoSorting()) {
                result.add(field);
            }
        }
        return result;
    }

    public static List<N2oQuery.Field> initDisplayFields(List<N2oQuery.Field> fields) {
        List<N2oQuery.Field> result = new ArrayList<>();
        for (N2oQuery.Field field : fields) {
            if (!field.getNoDisplay()) {
                result.add(field);
            }
        }
        return result;
    }

    public static  Map<String, N2oQuery.Field> initFieldsMap(List<N2oQuery.Field> fields, String id) {
        Map<String, N2oQuery.Field> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (N2oQuery.Field field : fields) {
            result.put(field.getId(), field);
        }
        return result;
    }


    public static Map<String, String> initFieldNamesMap(Map<String, N2oQuery.Field> fieldsMap) {
        return fieldsMap.values()
                .stream()
                .collect(Collectors.toMap(N2oQuery.Field::getId, N2oQuery.Field::getName));
    }

}
