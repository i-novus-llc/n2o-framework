package net.n2oapp.framework.autotest.impl.component.control;

import net.n2oapp.framework.autotest.api.component.control.Html;

/**
 * Компонент ввода html для автотестирования
 */
public class N2oHtml extends N2oControl implements Html {

    @Override
    public void shouldHaveValue(String value) {
        assert element().$("div").innerHtml().equals(value);
    }
}
