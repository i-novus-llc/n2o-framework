package net.n2oapp.framework.autotest.api.component;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

public interface Markdown extends Snippet {
    StandardButton button(String label);
}
