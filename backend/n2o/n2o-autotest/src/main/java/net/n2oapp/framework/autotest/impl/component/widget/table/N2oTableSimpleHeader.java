package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;

/**
 * Заголовок простого столбца таблицы для автотестирования
 */
public class N2oTableSimpleHeader extends N2oTableHeader implements TableSimpleHeader {

    @Override
    public void shouldHaveCssClass(String cssClass) {
        element().shouldHave(Condition.cssClass(cssClass));
    }
}
