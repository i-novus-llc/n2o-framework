package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Линия графика для автотестирования
 */
public interface Line extends Component {

    /**
     * Проверка цвета линии
     *
     * @param color ожидаемый цвет в HEX
     */
    void shouldHaveColor(String color);
}