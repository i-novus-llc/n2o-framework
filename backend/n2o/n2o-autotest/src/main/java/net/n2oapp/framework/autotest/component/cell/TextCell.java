package net.n2oapp.framework.autotest.component.cell;

import net.n2oapp.framework.autotest.N2oMatcher;

public interface TextCell extends Cell {
    void shouldHaveText(N2oMatcher which);
}
