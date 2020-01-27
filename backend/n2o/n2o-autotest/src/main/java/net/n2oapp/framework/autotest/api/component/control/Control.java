package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.Component;

public interface Control extends Component {
    void shouldBeEnabled();
    void shouldBeDisabled();
}
