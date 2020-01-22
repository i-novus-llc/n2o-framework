package net.n2oapp.framework.autotest.component.control;

import net.n2oapp.framework.autotest.component.Component;

public interface Control extends Component {
    void shouldBeEnabled();
    void shouldBeDisabled();
}
