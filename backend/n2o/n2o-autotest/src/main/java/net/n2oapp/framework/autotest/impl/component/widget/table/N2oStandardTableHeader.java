package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.table.StandardTableHeader;

/**
 * Стандартный заголовок столбца таблицы для автотестирования
 */
public class N2oStandardTableHeader extends N2oTableHeader implements StandardTableHeader {
    @Override
    public void titleShouldHave(Condition condition) {
        element().shouldHave(condition);
    }

    @Override
    public void click() {
        element().$("a").should(Condition.exist).click();
    }

    @Override
    public void shouldNotBeSorted() {
        element().$(".n2o-sorting-icon").shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeSortedByAsc() {
        element().$(".n2o-sorting-icon.fa-sort-amount-asc").should(Condition.exist);
    }

    @Override
    public void shouldBeSortedByDesc() {
        element().$(".n2o-sorting-icon.fa-sort-amount-desc").should(Condition.exist);
    }
}
