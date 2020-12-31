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
        SelenideElement elm = element().$(".n2o-advanced-table-header-title").exists() ?
                element() :
                element().$(".n2o-advanced-table-header-cell-content");
        elm.shouldHave(Condition.cssClass(cssClass));
    }
}
