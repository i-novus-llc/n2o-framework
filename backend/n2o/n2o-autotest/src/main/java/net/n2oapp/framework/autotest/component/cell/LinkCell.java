package net.n2oapp.framework.autotest.component.cell;

import net.n2oapp.framework.autotest.N2oMatcher;

public interface LinkCell extends Cell {
    void click();
    void shouldHaveHref(N2oMatcher which);
    void shouldHaveText(N2oMatcher which);
}
