package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.widget.table.TableFilterHeader;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Заголовок фильтруемого столбца таблицы для автотестирования
 */
public class N2oTableFilterHeader extends N2oTableSimpleHeader implements TableFilterHeader {
    @Override
    public <T extends Control> T filterControl(Class<T> componentClass) {
        return component(element().$(".n2o-advanced-table-filter-dropdown-popup"), StandardField.class).control(componentClass);
    }

    @Override
    public void openFilterDropdown() {
        element().$(".n2o-advanced-table-filter-btn .btn").click();
    }

    @Override
    public void clickSearchButton() {
        buttons().find(Condition.text("Искать")).click();
    }

    @Override
    public void clickResetButton() {
        buttons().find(Condition.text("Сбросить")).click();
    }

    protected SelenideElement filterDropdown() {
        return element().$(".n2o-advanced-table-filter-dropdown").shouldBe(Condition.exist);
    }

    protected ElementsCollection buttons() {
        return filterDropdown().$$(".n2o-advanced-table-filter-dropdown-buttons button");
    }
}
