package net.n2oapp.framework.autotest.component.header;

import net.n2oapp.framework.autotest.N2oMatcher;

public interface StandardTableHeader extends TableHeader {
    void shouldHaveTitle(N2oMatcher which);
    void click();
    void shouldNotBeSorted();
    void shouldBeSortedByAsc();
    void shouldBeSortedByDesc();
}
