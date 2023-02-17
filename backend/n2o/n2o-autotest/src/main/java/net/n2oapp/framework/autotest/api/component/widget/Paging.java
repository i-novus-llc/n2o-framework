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
     * Проверка соответствия макета пагинации
     * @param layout ожидаемый тип макета
     */
    void shouldHaveLayout(Layout layout);

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
     * Проверка отсутствия кнопки перехода на предыдущую страницу
     */
    void shouldNotHavePrev();

    /**
     * Проверка наличия кнопки перехода на предыдущую страницу
     */
    void shouldHavePrev();

    /**
     * Проверка метки кнопки перехода на предыдущую страницу
     * @param label ожидаемое значение метки
     */
    void shouldHavePrevLabel(String label);

    /**
     * Проверка иконки кнопки перехода на предыдущую страницу
     * @param icon ожидаемая иконка
     */
    void shouldHavePrevIcon(String icon);

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
    void shouldHaveNextLabel(String label);

    /**
     * Проверка иконки кнопки перехода на следующую страницу
     * @param icon ожидаемая иконка
     */
    void shouldHaveNextIcon(String icon);

    /**
     * Переход на следующую страницу
     */
    void selectNext();

    /**
     * Проверка отсутствия кнопки перехода на первую страницу
     */
    void shouldNotHaveFirst();

    /**
     * Проверка наличия кнопки перехода на первую страницу
     */
    void shouldHaveFirst();

    /**
     * Проверка метки кнопки перехода на первую страницу
     * @param label ожидаемое значение метки
     */
    void shouldHaveFirstLabel(String label);

    /**
     * Проверка иконки кнопки перехода на первую страницу
     * @param icon ожидаемая иконка
     */
    void shouldHaveFirstIcon(String icon);

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
     * Проверка метки кнопки перехода на последнюю страницу
     * @param label ожидаемое значение метки
     */
    void shouldHaveLastLabel(String label);

    /**
     * Проверка иконки кнопки перехода на последнюю страницу
     * @param icon ожидаемая иконка
     */
    void shouldHaveLastIcon(String icon);

    /**
     * Переход на последнюю страницу
     */
    void selectLast();


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
