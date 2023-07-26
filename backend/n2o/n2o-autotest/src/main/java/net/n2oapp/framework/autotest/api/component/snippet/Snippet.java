package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

public interface Snippet extends Component {

    /**
     * Проверка текста на соответствие
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text, Duration... duration);
}
