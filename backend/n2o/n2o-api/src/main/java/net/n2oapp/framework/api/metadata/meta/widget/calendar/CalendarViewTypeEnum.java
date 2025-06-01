package net.n2oapp.framework.api.metadata.meta.widget.calendar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Вид отображения календаря
 */
@RequiredArgsConstructor
@Getter
public enum CalendarViewTypeEnum implements N2oEnum {
    DAY("day", "day"),
    WEEK("week", "week"),
    WORK_WEEK("workWeek", "work_week"),
    MONTH("month", "month"),
    AGENDA("agenda", "agenda");

    private final String id;
    private final String title;
}