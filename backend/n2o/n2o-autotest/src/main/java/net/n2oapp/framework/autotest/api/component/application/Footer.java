package net.n2oapp.framework.autotest.api.component.application;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент footer для автотестирования
 */
public interface Footer extends Component {

    void shouldHaveLeftTextNamed(String brandName);

    void shouldHaveRightTextNamed(String brandName);

}
