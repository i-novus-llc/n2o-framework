package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Кнопка меню для автотестирования
 */
public interface MenuItem extends Component {
    void labelShouldHave(String text);

    void click();
}
