package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Область виджета диаграма-область для автотестирования
 */
public interface Area extends Component {

    void shouldHaveWidth(String width);

    void shouldHaveHeight(String height);

}
