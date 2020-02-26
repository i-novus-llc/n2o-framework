package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Кнопка хедера для автотестирования
 */
public interface MenuItem extends Component {
    void shouldHaveLabel(String text);

    void click();
}
