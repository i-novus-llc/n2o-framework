package net.n2oapp.framework.autotest.component.cell;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oMatcher;

public interface BadgeCell extends Cell {
    void shouldHaveColor(Colors color);
    void shouldHaveText(N2oMatcher which);
}
