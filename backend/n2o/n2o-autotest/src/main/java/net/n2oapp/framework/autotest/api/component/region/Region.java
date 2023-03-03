package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион для автотестирования
 */
public interface Region extends Component {

    /**
     * Проверка стиля региона
     * @param style ожидаемый стиль
     */
    void shouldHaveStyle(String style);
}
