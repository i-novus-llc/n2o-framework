package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Область виджета диаграмма-область для автотестирования
 */
public interface Area extends Component {

    /**
     * Проверка ширины графика
     * @param width ожидаемая ширина графика
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка высоты графика
     * @param height ожидаемая высота графика
     */
    void shouldHaveHeight(int height);
}
