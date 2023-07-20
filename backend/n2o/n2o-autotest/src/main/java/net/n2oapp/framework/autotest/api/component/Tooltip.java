package net.n2oapp.framework.autotest.api.component;

import java.time.Duration;

/**
 * Тултип компонента страницы
 */
public interface Tooltip extends Component {

    /**
     * Проверка того, что тултип пуст
     */
    void shouldBeEmpty();

    /**
     * Проверка того, что тултип содержит текст
     * @param text ожидаемый текст
     */
    void shouldHaveText(String[] text, Duration... duration);
}
