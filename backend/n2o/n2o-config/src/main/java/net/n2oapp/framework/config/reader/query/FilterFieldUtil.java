package net.n2oapp.framework.config.reader.query;

import net.n2oapp.criteria.filters.FilterType;

/**
 * Утилита для генерации FilterField фильтров разных типов
 */
public class FilterFieldUtil {

    public static String generateFilterField(String fieldId, FilterType filterType) {
        switch (filterType){
            case eq:
                return fieldId;
            case eqOrIsNull:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "EqOrIsNull." + suffix;
                }
                return fieldId + "EqOrIsNull";
            case contains:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "Contains." + suffix;
                }
                return fieldId + "Contains";
            case in:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    if (suffix.equals("id")) {
                        return fieldId.substring(0, lastDot) + "s*." + suffix;
                    }
                }
                return fieldId + "s";
            case inOrIsNull:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    if (suffix.equals("id")) {
                        return fieldId.substring(0, lastDot) + "InOrIsNull*." + suffix;
                    }
                }
                return fieldId + "InOrIsNull";
            case isNotNull:
                return fieldId + "IsNotNull";
            case isNull:
                return fieldId + "IsNull";
            case less:
                return fieldId + "Interval.end";
            case like:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "Like." + suffix;
                }
                return fieldId + "Like";
            case likeStart:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "LikeStart." + suffix;
                }
                return fieldId + "LikeStart";
            case more:
                return fieldId + "Interval.begin";
            case notEq:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "NotEq." + suffix;
                }
                return fieldId + "NotEq";
            case notIn:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    if (suffix.equals("id")) {
                        return fieldId.substring(0, lastDot) + "NotIn*." + suffix;
                    }
                }
                return fieldId + "NotIn";
            case overlaps:
                if (fieldId.contains(".")) {
                    int lastDot = fieldId.lastIndexOf(".");
                    String suffix = fieldId.substring(lastDot + 1);
                    return fieldId.substring(0, lastDot) + "Overlap." + suffix;
                }
                return fieldId + "Overlap";
            default:
                return fieldId;
        }
    }
}
