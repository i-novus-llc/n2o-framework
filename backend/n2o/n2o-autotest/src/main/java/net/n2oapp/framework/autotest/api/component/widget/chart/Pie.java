package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Круговая диаграмма для автотестирования
 */
public interface Pie extends Component {

    /**
     * Проверка количества секторов
     *
     * @param count ожидаемое количество
     */
    void shouldHaveSectorsCount(int count);

    /**
     * Проверка цвета секторов
     *
     * @param color ожидаемый цвет в HEX
     */
    void shouldHaveSectorColor(String color);

    /**
     * Проверка наличия подписей
     */
    void shouldHaveLabels();

    /**
     * Проверка наличия подписи с указанным текстом
     *
     * @param text ожидаемый текст подписи
     */
    void shouldHaveLabelText(String text);
}