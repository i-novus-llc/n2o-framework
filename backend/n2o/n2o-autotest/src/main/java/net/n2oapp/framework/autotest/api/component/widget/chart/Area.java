package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Область виджета диаграмма-область для автотестирования
 */
public interface Area extends Component {

    /**
     * Проверка цвета заливки области
     *
     * @param color ожидаемый цвет в HEX
     */
    void shouldHaveColor(String color);

    /**
     * Проверка цвета обводки области
     *
     * @param color ожидаемый цвет в HEX
     */
    void shouldHaveStrokeColor(String color);
}