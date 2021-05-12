package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион для автотестирования
 */
public interface Region extends Component {
    void shouldHaveCssClass(String classname);

    void shouldHaveStyle(String style);
}
