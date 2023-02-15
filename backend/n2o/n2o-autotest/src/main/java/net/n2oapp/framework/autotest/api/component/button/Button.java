package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.api.component.badge.Badge;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Кнопка для автотестирования
 */
public interface Button extends Component, Badge {
    /**
     * Проверка на не доступность клика по кнопке
     */
    void shouldBeDisabled();

    /**
     * Проверка на не доступность клика по кнопке
     */
    void shouldBeEnabled();

    /**
     * Клик по кнопке
     */
    void click();
}
