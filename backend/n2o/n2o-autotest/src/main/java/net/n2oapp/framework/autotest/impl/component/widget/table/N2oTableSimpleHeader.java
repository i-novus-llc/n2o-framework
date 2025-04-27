package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import org.openqa.selenium.interactions.Actions;

/**
 * Заголовок простого столбца таблицы для автотестирования
 */
public class N2oTableSimpleHeader extends N2oTableHeader implements TableSimpleHeader {

    @Override
    public void shouldBeDraggable() {
        element().shouldHave(Condition.attribute("data-draggable", "true"));
    }

    @Override
    public void shouldNotBeDraggable() {
        element().shouldNotHave(Condition.attribute("data-draggable"));
    }

    @Override
    public void shouldHaveDndIcon() {
        element().should(Condition.cssClass("drag-header"));
    }

    @Override
    public void shouldNotHaveDndIcon() {
        element().shouldNotHave(Condition.cssClass("drag-header"));

    }

    @Override
    public void dragAndDropTo(TableSimpleHeader header) {
        SelenideElement dndElement = element().parent().$(".drag-header").$(".fa-ellipsis-v");
        Actions actions = new Actions(Selenide.webdriver().driver().getWebDriver());
        actions.clickAndHold(dndElement)
                .moveToElement(header.element())
                .release()
                .perform();
    }
}
