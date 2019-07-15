package net.n2oapp.criteria.filters;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Checker for check value by filter
 */
public class FilterChecker {


    /**
     * Checks that the value satisfies the filter constraints
     * @param filter  filter
     * @param value value for checking
     * @return  <tt>true</tt> if the value satisfies the filter constraints, else <tt>false</tt>
     */
    @SuppressWarnings("unchecked")
    public static boolean check(Filter filter, Object value) {
        switch (filter.getType()) {
            case eq:
                return equals(filter.getValue(), value);
            case notEq:
                return !equals(filter.getValue(), value);
            case in:
                return containsOne((List) filter.getValue(), value);
            case notIn:
                return !containsOne((List) filter.getValue(), value);
            case more:
                return compare(filter.getValue(), value) < 0;
            case less:
                return compare(filter.getValue(), value) > 0;
            case isNull:
                return value == null;
            case isNotNull:
                return value != null;
            case eqOrIsNull:
                return equals(filter.getValue(), value) || value == null;
            case inOrIsNull:
                return contains((List) filter.getValue(), value) || value == null;
            case like:
                return like(filter.getValue(), value);
            case likeStart:
                return likeStart(filter.getValue(), value);
            case overlaps:
                return overlap((List) filter.getValue(), value);
            case contains:
                return contains((List) filter.getValue(), value);
            default:
                throw new IllegalArgumentException(String.format("Unknown filter-type '%s'", filter.getType()));
        }
    }

    private static boolean equals(Object filterValue, Object realValue) {
        filterValue = castToRealType(filterValue, realValue);
        if (filterValue instanceof Number && realValue instanceof Number) {
            return ((Number) filterValue).longValue() == ((Number) realValue).longValue();
        }
        return filterValue.equals(realValue);
    }

    private static boolean containsOne(List<?> values, Object value) {
        return values.stream().anyMatch(fv -> equals(fv, value));
    }

    private static boolean contains(List<?> values, Object value) {
        if (value instanceof List) {
            final boolean[] res = {true};
            values.forEach(v -> {
                if (!containsOne((List<?>) value, castToRealType(v, ((List<?>) value).get(0)))) {
                    res[0] = false;
                }
            });
            return res[0];
        }
        return containsOne(values, value);
    }

    private static boolean overlap(List<?> values, Object value) {
        if (value instanceof List) {
            return values.stream().anyMatch(v -> contains((List<?>)value, v));
        }
        return containsOne(values, value);
    }

    @SuppressWarnings("unchecked")
    private static int compare(Object filterValue, Object realValue) {
        Object value = castToRealType(filterValue, realValue);
        if (value instanceof Number && realValue instanceof Number) {
            return (int) (((Number) value).longValue() - ((Number) realValue).longValue());
        }
        return ((Comparable) value).compareTo(realValue);
    }

    private static boolean like(Object filterValue, Object realValue) {
        if (!(filterValue instanceof String) || !(realValue instanceof String))
            return false;
        return ((String) filterValue).matches(".*" + realValue + ".*");
    }

    private static boolean likeStart(Object filterValue, Object realValue) {
        if (!(filterValue instanceof String) || !(realValue instanceof String))
            return false;
        return ((String) filterValue).matches(realValue + ".*");
    }

    private static Object castToRealType(Object filterValue, Object realValue) {
        if (filterValue instanceof String && !(realValue instanceof String)) {
            String strValue = (String) filterValue;
            if (realValue instanceof Boolean && (strValue.equals("true") || strValue.equals("false")))
                return Boolean.valueOf(strValue);
            if (realValue instanceof Number && strValue.matches("([\\d]+)")) {
                try {
                    return Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    return filterValue;
                }
            }
            if (realValue instanceof UUID)
                return UUID.fromString((String) filterValue);
        } else if (filterValue != null && !(filterValue instanceof String) && realValue instanceof String) {
            return filterValue.toString();
        }
        return filterValue;
    }
}
