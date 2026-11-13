package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Столбец гистограммы для автотестирования
 */
public interface Bar extends Component {

    /**
     * Проверка цвета столбца
     *
     * @param color ожидаемый цвет в HEX
     */
    void shouldHaveColor(String color);
}