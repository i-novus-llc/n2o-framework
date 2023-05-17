package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.table.TableMultiHeader;

/**
 * Заголовок мультистолбца таблицы для автотестирования
 */
public class N2oTableMultiHeader extends N2oTableHeader implements TableMultiHeader {

    @Override
    public void shouldHaveCssClass(String cssClass) {
        (element().$(".n2o-advanced-table-header-cell-content")
                .exists() ? element().$(".n2o-advanced-table-header-cell-content") : element())
                .shouldHave(Condition.cssClass(cssClass));
    }
}
