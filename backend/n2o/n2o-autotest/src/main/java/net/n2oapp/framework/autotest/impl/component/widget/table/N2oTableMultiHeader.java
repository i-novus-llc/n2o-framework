package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.table.TableMultiHeader;

/**
 * Multi заголовок столбца таблицы для автотестирования
 */
public class N2oTableMultiHeader extends N2oStandardTableHeader implements TableMultiHeader {

    @Override
    public void shouldHaveCssClass(String cssClass) {
        if (element().$(".n2o-advanced-table-header-title").exists()) {
            element()
                    .shouldHave(Condition.attributeMatching("class", ".*" + cssClass + ".*"));
        } else {
            element().$("div.n2o-advanced-table-header-cell-content")
                    .shouldHave(Condition.attributeMatching("class", ".*" + cssClass + ".*"));
        }
    }
}
