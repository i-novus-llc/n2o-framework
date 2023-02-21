package net.n2oapp.framework.autotest.api.component.application;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент footer для автотестирования
 */
public interface Footer extends Component {
    /**
     * Проверка точного соответствия текста (без учета регистра) в левой части компонента footer
     * @param name ожидаемое значение текста
     */
    void leftTextShouldHaveValue(String name);

    /**
     * Проверка точного соответствия текста (без учета регистра) в правой части компонента footer
     * @param name ожидаемое значение текста
     */
    void rightTextShouldHaveValue(String name);

}
