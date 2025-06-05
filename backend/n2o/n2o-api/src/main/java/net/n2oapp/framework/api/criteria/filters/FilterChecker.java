package net.n2oapp.framework.api.criteria.filters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Checker for check value by filter
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterChecker {


    /**
     * Checks that the value satisfies the filter constraints
     *
     * @param filter filter
     * @param value  value for checking
     * @return <tt>true</tt> if the value satisfies the filter constraints, else <tt>false</tt>
     */
    @SuppressWarnings("unchecked")
    public static boolean check(Filter filter, Object value) {
        switch (filter.getType()) {
            case EQ:
                return equals(filter.getValue(), value);
            case NOT_EQ:
                return !equals(filter.getValue(), value);
            case IN:
                return containsOne((List) filter.getValue(), value);
            case NOT_IN:
                return !containsOne((List) filter.getValue(), value);
            case MORE:
                return compare(filter.getValue(), value) < 0;
            case LESS:
                return compare(filter.getValue(), value) > 0;
            case IS_NULL:
                return value == null;
            case IS_NOT_NULL:
                return value != null;
            case EQ_OR_IS_NULL:
                return equals(filter.getValue(), value) || value == null;
            case IN_OR_IS_NULL:
                return contains((List) filter.getValue(), value) || value == null;
            case LIKE:
                return like(filter.getValue(), value);
            case LIKE_START:
                return likeStart(filter.getValue(), value);
            case OVERLAPS:
                return overlap((List) filter.getValue(), value);
            case CONTAINS:
                return contains((List) filter.getValue(), value);
            default:
                throw new IllegalArgumentException(String.format("Unknown filter-type '%s'", filter.getType()));
        }
    }

    private static boolean equals(Object filterValue, Object realValue) {
        filterValue = castToRealType(filterValue, realValue);
        if (filterValue == null)
            return false;
        if (filterValue instanceof Number filterNumber && realValue instanceof Number realNumber) {
            return filterNumber.longValue() == realNumber.longValue();
        }
        return filterValue.equals(realValue);
    }

    private static boolean containsOne(List<?> values, Object value) {
        return values.stream().anyMatch(fv -> equals(fv, value));
    }

    private static boolean contains(List<?> values, Object value) {
        if (value instanceof List valueList) {
            if (values == null || values.isEmpty() || valueList.isEmpty()) {
                return false;
            }
            final boolean[] res = {true};
            values.forEach(v -> {
                if (!containsOne(valueList, castToRealType(v, valueList.get(0)))) {
                    res[0] = false;
                }
            });
            return res[0];
        }
        return containsOne(values, value);
    }

    private static boolean overlap(List<?> values, Object value) {
        if (value instanceof List valueList) {
            return values.stream().anyMatch(v -> contains(valueList, v));
        }
        return containsOne(values, value);
    }

    @SuppressWarnings("unchecked")
    private static int compare(Object filterValue, Object realValue) {
        Object value = castToRealType(filterValue, realValue);
        if (value == null)
            return 0;
        if (value instanceof Number valueNumber && realValue instanceof Number realNumber) {
            return (int) (valueNumber.longValue() - realNumber.longValue());
        }
        return ((Comparable) value).compareTo(realValue);
    }

    private static boolean like(Object filterValue, Object realValue) {
        if (!(filterValue instanceof String filterString) || !(realValue instanceof String realString))
            return false;
        return filterString.matches(".*" + realString + ".*");
    }

    private static boolean likeStart(Object filterValue, Object realValue) {
        if (!(filterValue instanceof String filterString) || !(realValue instanceof String realString))
            return false;
        return filterString.matches(realString + ".*");
    }

    private static Object castToRealType(Object filterValue, Object realValue) {
        if (filterValue instanceof String strValue && !(realValue instanceof String)) {
            if (realValue instanceof Boolean realValueBoolean && (strValue.equals("true") || strValue.equals("false")))
                return realValueBoolean;
            if (realValue instanceof Number && strValue.matches("([\\d]+)")) {
                try {
                    return Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    return filterValue;
                }
            }
            if (realValue instanceof UUID)
                return UUID.fromString(strValue);
        } else if (filterValue != null && !(filterValue instanceof String) && realValue instanceof String) {
            return filterValue.toString();
        }
        return filterValue;
    }
}
