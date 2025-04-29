package net.n2oapp.framework.api.metadata.meta.widget.calendar;

/**
 * Вид отображения календаря
 */
public enum CalendarViewTypeEnum {
    day,
    week,
    workWeek("work_week"),
    month,
    agenda;

    private String title;

    CalendarViewTypeEnum() {
        this.title = this.name();
    }

    CalendarViewTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
