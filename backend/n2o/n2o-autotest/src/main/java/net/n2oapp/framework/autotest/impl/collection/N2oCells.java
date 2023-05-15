package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;


import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oCells extends N2oComponentsCollection implements Cells {

    private static final String ROW_CLICK_CSS_CONDITION = "row-click";

    @Override
    public TextCell cell(int index) {
        return component(elements().get(index), TextCell.class);
    }

    @Override
    public <T extends Cell> T cell(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }

    @Override
    public <T extends Cell> T cell(Condition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }

    @Override
    public void click() {
        row().click();
    }

    @Override
    public void shouldBeClickable() {
        row().shouldHave(Condition.cssClass(ROW_CLICK_CSS_CONDITION));
    }

    @Override
    public void shouldNotBeClickable() {
        row().shouldNotHave(Condition.cssClass(ROW_CLICK_CSS_CONDITION));
    }

    @Override
    public void hover() {
        row().hover();
    }

    @Override
    public void shouldHaveColor(Colors color) {
        row().shouldHave(Condition.cssClass(color.name("bg-")));
    }

    protected SelenideElement row() {
        return elements().get(0).parent();
    }
}
