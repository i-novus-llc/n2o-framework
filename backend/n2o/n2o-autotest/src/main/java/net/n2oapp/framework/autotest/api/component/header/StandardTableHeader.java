package net.n2oapp.framework.autotest.api.component.header;

import com.codeborne.selenide.Condition;

public interface StandardTableHeader extends TableHeader {
    void titleShouldHave(Condition condition);
    void click();
    void shouldNotBeSorted();
    void shouldBeSortedByAsc();
    void shouldBeSortedByDesc();
}
