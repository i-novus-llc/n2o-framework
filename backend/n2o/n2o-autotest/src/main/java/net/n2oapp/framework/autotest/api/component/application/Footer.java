package net.n2oapp.framework.autotest.api.component.application;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент footer для автотестирования
 */
public interface Footer extends Component {
    /**
     * Проверка значения текста в левой части компонента footer
     * @param brandName ожидаемое значение текста
     */
    void shouldHaveLeftTextNamed(String brandName);

    /**
     * Проверка значения текста в правой части компонента footer
     * @param brandName ожидаемое значение текста
     */
    void shouldHaveRightTextNamed(String brandName);

}
