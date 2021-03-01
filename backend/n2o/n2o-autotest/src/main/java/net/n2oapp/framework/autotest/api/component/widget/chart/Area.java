package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Область виджета диаграмма-область для автотестирования
 */
public interface Area extends Component {

    void shouldHaveWidth(int width);

    void shouldHaveHeight(int height);
}
