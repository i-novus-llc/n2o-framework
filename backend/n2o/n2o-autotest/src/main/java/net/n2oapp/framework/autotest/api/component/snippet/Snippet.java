package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.api.component.Component;

public interface Snippet extends Component {
    void shouldHaveText(String text);
}
