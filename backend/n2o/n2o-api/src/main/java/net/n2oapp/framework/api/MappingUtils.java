package net.n2oapp.framework.api;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.spel;

/**
 * Утилитный класс для работы с маппингами
 */
public class MappingUtils {

    private MappingUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Конкатенация двух маппингов в один
     *
     * @param child  Дочерний маппинг
     * @param parent Родительский маппинг
     * @return сконкатенированный маппинг
     * Пример:
     * ['organization'], ['id'] -> ['organization.id']
     */
    public static String concatMappings(String child, String parent) {
        return spel(parent != null ? StringUtils.unwrapSpel(parent) + "." + StringUtils.unwrapSpel(child) : StringUtils.unwrapSpel(child));
    }

    /**
     * Добавляет индекс  в маппинг
     *
     * Примеры:
     * ['departments'] -> ['departments[i]']
     * ['departments.employees'] -> ['departments.employees[i]']
     */
    public static String addIndex(String mapping) {
        return spel(StringUtils.unwrapSpel(mapping) + "[i]");
    }

    /**
     * Резолв первого индекса вида [i] в маппинге
     *
     * Примеры:
     * ['departments.employees[i]'], 3 -> ['departments.employees[3]']
     * ['departments.employees[i].emails[i]'], 0 -> ['departments.employees[0].emails[i]']
     *
     * @param mapping Маппинг
     * @param index   Индекс для посдтановки
     * @return Разрезолвленный маппинг
     */
    public static String resolveFirstIndex(String mapping, int index) {
        return mapping.replaceFirst("\\[i\\]", "[" + index + "]");
    }

    /**
     * Получение последнего дочернего маппинга
     *
     * Пример:
     * ['organization.employee.name'] -> ['name']
     */
    public static String getLastChild(String mapping) {
        String[] split = StringUtils.unwrapSpel(mapping).split("\\.");
        if (split.length == 0)
            return mapping;
        return spel(split[split.length -1]);
    }
}
