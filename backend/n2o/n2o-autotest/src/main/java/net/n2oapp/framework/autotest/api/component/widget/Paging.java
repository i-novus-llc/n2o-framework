package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент пагинации для автотестирования
 */
public interface Paging extends Component {

    /**
     * Проверка активной/выбранной страницы
     * @param number ожидаемый номер выбранной страницы
     */
    void shouldHaveActivePage(String number);

    /**
     * Клик по номеру страницы
     * @param number номер страницы
     */
    void selectPage(String number);

    /**
     * Проверка наличия номера страницы
     * @param number проверяемый номер страницы
     */
    void shouldHavePageNumber(String number);

    /**
     * Возвращает общее количества элементов у виджета
     * @return Число элементов
     */
    int totalElements();

    /**
     * Проверка общего числа элементов у виджета
     * @param count ожидаемое количество
     */
    void shouldHaveTotalElements(int count);

    /**
     * Проверка отсутствия отображения общего числа элементов
     */
    void shouldNotHaveTotalElements();

    /**
     * Проверка наличия кнопки перехода на предыдущую страницу
     */
    void shouldHavePrev();

    /**
     * Проверка отсутствия кнопки перехода на предыдущую страницу
     */
    void shouldNotHavePrev();

    /**
     * Проверка доступности кнопки перехода на предыдущую страницу
     */
    void prevButtonShouldBeEnabled();

    /**
     * Проверка недоступности кнопки перехода на предыдущую страницу
     */
    void prevButtonShouldBeDisabled();

    /**
     * Проверка метки кнопки перехода на предыдущую страницу
     * @param label ожидаемое значение метки
     */
    void prevShouldHaveLabel(String label);

    /**
     * Проверка иконки кнопки перехода на предыдущую страницу
     * @param icon ожидаемая иконка
     */
    void prevShouldHaveIcon(String icon);

    /**
     * Переход на предыдущую страницу
     */
    void selectPrev();

    /**
     * Проверка отсутствия кнопки перехода на следующую страницу
     */
    void shouldNotHaveNext();

    /**
     * Проверка наличия кнопки перехода на следующую страницу
     */
    void shouldHaveNext();

    /**
     * Проверка метки кнопки перехода на следующую страницу
     * @param label ожидаемое значение метки
     */
    void nextShouldHaveLabel(String label);

    /**
     * Проверка иконки кнопки перехода на следующую страницу
     * @param icon ожидаемая иконка
     */
    void nextShouldHaveIcon(String icon);

    /**
     * Переход на следующую страницу
     */
    void selectNext();

    /**
     * Проверка доступности кнопки перехода на следующую страницу
     */
    void nextButtonShouldBeEnabled();

    /**
     * Проверка недоступности кнопки перехода на следующую страницу
     */
    void nextButtonShouldBeDisabled();

    /**
     * Проверка наличия кнопки перехода на первую страницу
     */
    void shouldHaveFirst();

    /**
     * Переход на первую страницу
     */
    void selectFirst();

    /**
     * Проверка отсутствия кнопки перехода на последнюю страницу
     */
    void shouldNotHaveLast();

    /**
     * Проверка наличия кнопки перехода на последнюю страницу
     */
    void shouldHaveLast();

    /**
     * Проверка значения кнопки перехода на последнюю страницу
     * @param page ожидаемое значение кнопки
     */
    void lastShouldHavePage(String page);

    /**
     * Переход на последнюю страницу
     */
    void selectLast();

    /**
     * Проверки наличия многоточия у кнопки перехода на первую страницу
     */
    void firstPageShouldHaveEllipsis();

    /**
     * Проверки отсутствия многоточия у кнопки перехода на первую страницу
     */
    void firstPageShouldNotHaveEllipsis();

    /**
     * Проверки наличия многоточия у кнопки перехода на последнюю страницу
     */
    void lastPageShouldHaveEllipsis();

    /**
     * Проверки отсутствия многоточия у кнопки перехода на последнюю страницу
     */
    void lastPageShouldNotHaveEllipsis();

    /**
     * Проверка видимости кнопки перехода на страницу
     * @param number ожидаемая страница
     */
    void pageNumberButtonShouldBeVisible(String number);

    /**
     * Проверка отсутствия видимости кнопки перехода на страницу
     * @param number ожидаемая страница
     */
    void pageNumberButtonShouldNotBeVisible(String number);

    /**
     * Проверка видимости кнопки запроса на количество записей
     */
    void countButtonShouldBeVisible();

    /**
     * Проверка отсутствия видимости кнопки запроса на количество записей
     */
    void countButtonShouldNotBeVisible();

    /**
     * Клик по "Узнать кол-во записей"
     */
    void countButtonClick();

    enum Layout {
        BORDERED("bordered"),
        FLAT("flat"),
        SEPARATED("separated"),
        BORDERED_ROUNDED("bordered-rounded"),
        FLAT_ROUNDED("flat-rounded"),
        SEPARATED_ROUNDED("separated-rounded");

        private final String title;

        Layout(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
