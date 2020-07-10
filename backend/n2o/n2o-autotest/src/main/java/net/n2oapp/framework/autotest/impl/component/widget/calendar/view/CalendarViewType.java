package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

/**
 * Типы отображения календаря для автотестирования
 */
public enum CalendarViewType {
    MONTH("Месяц"),
    DAY("День"),
    AGENDA("Повестка дня"),
    WEEK("Неделя"),
    WORK_WEEK("Рабочая неделя");

    private String title;

    CalendarViewType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
