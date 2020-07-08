package net.n2oapp.framework.api.metadata.meta.widget.calendar;

/**
 * Вид отображения календаря
 */
public enum CalendarViewType {
    day,
    week,
    workWeek("work_week"),
    month,
    agenda;

    private String title;

    CalendarViewType() {
        this.title = this.name();
    }

    CalendarViewType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
